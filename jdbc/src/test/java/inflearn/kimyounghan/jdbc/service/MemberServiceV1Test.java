package inflearn.kimyounghan.jdbc.service;

import inflearn.kimyounghan.jdbc.domain.Member;
import inflearn.kimyounghan.jdbc.repository.MemberJdbcRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceV1Test {

    private final MemberJdbcRepository memberJdbcRepository = new MemberJdbcRepository();
    private final MemberServiceV1 memberServiceV1 = new MemberServiceV1(memberJdbcRepository);

    @BeforeEach
    void setup() throws SQLException {
        memberJdbcRepository.deleteById("memberA");
        memberJdbcRepository.deleteById("memberB");
        memberJdbcRepository.deleteById("ex");
    }

    @DisplayName("정상 이체")
    @Test
    void whenValidMembers_thenPass() throws SQLException {
        // given
        Member memberA = new Member("memberA", 10000);
        Member memberB = new Member("memberB", 10000);
        memberJdbcRepository.save(memberA);
        memberJdbcRepository.save(memberB);

        // when
        memberServiceV1.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        // then
        Member memberAFound = memberJdbcRepository.findById(memberA.getMemberId()).orElseThrow();
        Member memberBFound = memberJdbcRepository.findById(memberB.getMemberId()).orElseThrow();

        assertThat(memberAFound.getMoney()).isEqualTo(8000);
        assertThat(memberBFound.getMoney()).isEqualTo(12000);
    }

    @DisplayName("이체 예외 비정상 사용자")
    @Test
    void whenInvalidMember_thenThrow() throws SQLException {
        // given
        Member memberA = new Member("memberA", 10000);
        Member memberEx = new Member("ex", 10000);
        memberJdbcRepository.save(memberA);
        memberJdbcRepository.save(memberEx);

        // when
        Assertions.assertThatThrownBy(() ->
                memberServiceV1.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        Member memberAFound = memberJdbcRepository.findById(memberA.getMemberId()).orElseThrow();
        Member memberBFound = memberJdbcRepository.findById(memberEx.getMemberId()).orElseThrow();
        assertThat(memberAFound.getMoney()).isEqualTo(8000);
        assertThat(memberBFound.getMoney()).isEqualTo(10000);
    }

}
