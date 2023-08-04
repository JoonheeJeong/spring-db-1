package inflearn.kimyounghan.jdbc.repository;

import inflearn.kimyounghan.jdbc.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class MemberJdbcRepositoryTest {

    private final MemberJdbcRepository repo = new MemberJdbcRepository();

    @Test
    void save() throws SQLException {
        final String memberId = "member1";
        final Integer money = 20000;
        Member member = repo.save(new Member(memberId, money));

        Member foundMember = repo.findById(memberId).orElseThrow();
        assertThat(foundMember).isNotSameAs(member);
        assertThat(foundMember).isEqualTo(member);
    }

    @Test
    void findById() throws SQLException {
        final String memberId = "member1";
        Member member = repo.findById(memberId).orElseThrow();

        assertThat(member.getMemberId()).isEqualTo(memberId);
    }

    @Test
    void update() throws SQLException {
        final String memberId = "member1";
        final Integer updatedMoney = 30000;
        Member updatedMember = new Member(memberId, updatedMoney);
        repo.update(updatedMember);

        Member foundMember = repo.findById(memberId).orElseThrow();
        assertThat(foundMember).isEqualTo(updatedMember);
    }

}