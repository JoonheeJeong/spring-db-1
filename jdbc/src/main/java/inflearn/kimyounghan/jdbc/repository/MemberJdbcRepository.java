package inflearn.kimyounghan.jdbc.repository;

import inflearn.kimyounghan.jdbc.connection.DBConnectionUtil;
import inflearn.kimyounghan.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class MemberJdbcRepository {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member (member_id, money) values (?, ?)";

        Connection conn = null;
        PreparedStatement prepStmt = null;
        try {
            conn = DBConnectionUtil.getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, member.getMemberId());
            prepStmt.setInt(2, member.getMoney());
            prepStmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("MemberJdbcRepository#save", e);
            throw e;
        } finally {
            DBConnectionUtil.close(conn, prepStmt, null);
        }
    }
}
