package inflearn.kimyounghan.jdbc.repository;

import inflearn.kimyounghan.jdbc.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemberJdbcRepositoryTest {

    private final MemberJdbcRepository repo = new MemberJdbcRepository();

    @BeforeEach
    void setup() {
//        db has Member("member1", 20000)
    }

    @Test
    void save() throws SQLException {
        final String memberId = "member2";
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
        final int updatedMoney = 20000;
        Member foundMember = repo.findById(memberId).orElseThrow();
        foundMember.updateMoney(updatedMoney);
        Member updatedMember = repo.update(foundMember);

        foundMember = repo.findById(memberId).orElseThrow();
        assertThat(foundMember).isEqualTo(updatedMember);
    }

    @Test
    void deleteById() throws SQLException {
        final String memberId = "member1";
        repo.deleteById(memberId);

        Optional<Member> byId = repo.findById(memberId);
        assertThat(byId).isEmpty();
    }

}