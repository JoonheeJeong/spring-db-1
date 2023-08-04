package inflearn.kimyounghan.jdbc.repository;

import inflearn.kimyounghan.jdbc.domain.Member;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class MemberJdbcRepositoryTest {

    private final MemberJdbcRepository repo = new MemberJdbcRepository();

    @Test
    void save() throws SQLException {
        Member member = new Member("member2", 10000);
        repo.save(member);
    }

}