package inflearn.kimyounghan.jdbc.service;

import inflearn.kimyounghan.jdbc.domain.Member;
import inflearn.kimyounghan.jdbc.repository.MemberJdbcRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class AutoRegistrationTest {

    @Autowired
    private MemberJdbcRepositoryV3 memberRepository;
    @Autowired
    private MemberTransactionAopService memberService;

    @RequiredArgsConstructor
    @TestConfiguration
    static class TestConfig {

        private final DataSource dataSource;

        @Bean
        MemberJdbcRepositoryV3 memberRepository() {
            return new MemberJdbcRepositoryV3(dataSource);
        }

        @Bean
        MemberTransactionAopService memberService() {
            return new MemberTransactionAopService(memberRepository());
        }
    }

    @BeforeEach
    void setup() throws SQLException {
        memberRepository.deleteById("memberA");
        memberRepository.deleteById("memberB");
        memberRepository.deleteById("ex");
    }

    @DisplayName("AOP 프록시 생성 확인")
    @Test
    void aopProxyGenerationCheck() {
        log.info("memberRepository class: {}", memberRepository.getClass());
        log.info("memberService class: {}", memberService.getClass());

        assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
        assertThat(AopUtils.isAopProxy(memberService)).isTrue();
    }

    @DisplayName("정상 이체 커밋")
    @Test
    void whenValidMembers_thenPass() throws SQLException {
        // given
        Member memberA = new Member("memberA", 10000);
        Member memberB = new Member("memberB", 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        // when
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        // then
        Member memberAFound = memberRepository.findById(memberA.getMemberId()).orElseThrow();
        Member memberBFound = memberRepository.findById(memberB.getMemberId()).orElseThrow();

        assertThat(memberAFound.getMoney()).isEqualTo(8000);
        assertThat(memberBFound.getMoney()).isEqualTo(12000);
    }

    @DisplayName("이체 예외 비정상 사용자 롤백")
    @Test
    void whenInvalidMember_thenThrow() throws SQLException {
        // given
        Member memberA = new Member("memberA", 10000);
        Member memberEx = new Member("ex", 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);

        // when
        assertThatThrownBy(() -> memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        Member memberAFound = memberRepository.findById(memberA.getMemberId()).orElseThrow();
        Member memberBFound = memberRepository.findById(memberEx.getMemberId()).orElseThrow();
        assertThat(memberAFound.getMoney()).isEqualTo(10000);
        assertThat(memberBFound.getMoney()).isEqualTo(10000);
    }
}