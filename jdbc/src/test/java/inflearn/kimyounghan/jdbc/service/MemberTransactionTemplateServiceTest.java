package inflearn.kimyounghan.jdbc.service;

import inflearn.kimyounghan.jdbc.connection.DBConnectionUtil;
import inflearn.kimyounghan.jdbc.domain.Member;
import inflearn.kimyounghan.jdbc.repository.MemberJdbcRepositoryV3;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTransactionTemplateServiceTest {

    private final MemberJdbcRepositoryV3 memberJdbcRepositoryV3 = new MemberJdbcRepositoryV3(DriverManagerDataSource.class);
    private final MemberTransactionTemplateService memberTransactionTemplateService = new MemberTransactionTemplateService(
            new DataSourceTransactionManager(DBConnectionUtil.getInstance().getDataSource()),
            memberJdbcRepositoryV3);

    @BeforeEach
    void setup() throws SQLException {
        memberJdbcRepositoryV3.deleteById("memberA");
        memberJdbcRepositoryV3.deleteById("memberB");
        memberJdbcRepositoryV3.deleteById("ex");
    }

    @DisplayName("정상 이체 커밋")
    @Test
    void whenValidMembers_thenPass() throws SQLException {
        // given
        Member memberA = new Member("memberA", 10000);
        Member memberB = new Member("memberB", 10000);
        memberJdbcRepositoryV3.save(memberA);
        memberJdbcRepositoryV3.save(memberB);

        // when
        memberTransactionTemplateService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        // then
        Member memberAFound = memberJdbcRepositoryV3.findById(memberA.getMemberId()).orElseThrow();
        Member memberBFound = memberJdbcRepositoryV3.findById(memberB.getMemberId()).orElseThrow();

        assertThat(memberAFound.getMoney()).isEqualTo(8000);
        assertThat(memberBFound.getMoney()).isEqualTo(12000);
    }

    @DisplayName("이체 예외 비정상 사용자 롤백")
    @Test
    void whenInvalidMember_thenThrow() throws SQLException {
        // given
        Member memberA = new Member("memberA", 10000);
        Member memberEx = new Member("ex", 10000);
        memberJdbcRepositoryV3.save(memberA);
        memberJdbcRepositoryV3.save(memberEx);

        // when
        Assertions.assertThatThrownBy(() ->
                        memberTransactionTemplateService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        Member memberAFound = memberJdbcRepositoryV3.findById(memberA.getMemberId()).orElseThrow();
        Member memberBFound = memberJdbcRepositoryV3.findById(memberEx.getMemberId()).orElseThrow();
        assertThat(memberAFound.getMoney()).isEqualTo(10000);
        assertThat(memberBFound.getMoney()).isEqualTo(10000);
    }

}
