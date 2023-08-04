package inflearn.kimyounghan.jdbc.repository;

import inflearn.kimyounghan.jdbc.connection.DBConnectionUtil;
import inflearn.kimyounghan.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

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

    public Optional<Member> findById(String memberId) throws SQLException {
        String sql = "select money from member where member_id = ? ";

        Connection conn = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnectionUtil.getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, memberId);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                Integer money = rs.getInt(1);
                return Optional.of(new Member(memberId, money));
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("MemberJdbcRepository#findById", e);
            throw e;
        } finally {
            DBConnectionUtil.close(conn, prepStmt, rs);
        }
    }

    public Member update(Member member) throws SQLException {
        String sql = "update member set money = ? where member_id = ?";

        Connection conn = null;
        PreparedStatement prepStmt = null;
        try {
            conn = DBConnectionUtil.getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setInt(1, member.getMoney());
            prepStmt.setString(2, member.getMemberId());
            prepStmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("MemberJdbcRepository#update", e);
            throw e;
        } finally {
            DBConnectionUtil.close(conn, prepStmt, null);
        }
    }
}
