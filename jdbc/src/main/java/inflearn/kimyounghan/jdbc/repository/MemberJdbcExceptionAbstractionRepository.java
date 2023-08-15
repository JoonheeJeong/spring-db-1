package inflearn.kimyounghan.jdbc.repository;

import inflearn.kimyounghan.jdbc.connection.DBConnectionUtil;
import inflearn.kimyounghan.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * 스프링 예외 추상화를 통해 예외 누수 해결
 */
@Slf4j
public class MemberJdbcExceptionAbstractionRepository implements MemberRepository {

    private final DBConnectionUtil dbConnectionUtil;
    private final SQLExceptionTranslator exTranslator;

    public MemberJdbcExceptionAbstractionRepository(DataSource dataSource) {
        this.dbConnectionUtil = DBConnectionUtil.getInstance();
        this.dbConnectionUtil.setDateSource(dataSource);
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member (member_id, money) values (?, ?)";

        Connection conn = null;
        PreparedStatement prepStmt = null;
        try {
            conn = dbConnectionUtil.getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, member.getMemberId());
            prepStmt.setInt(2, member.getMoney());
            prepStmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            throw exTranslator.translate("save", sql, e);
        } finally {
            dbConnectionUtil.close(conn, prepStmt, null);
        }
    }

    @Override
    public Optional<Member> findById(String memberId) {
        String sql = "select money from member where member_id = ? ";

        Connection conn = null;
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        try {
            conn = dbConnectionUtil.getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, memberId);
            rs = prepStmt.executeQuery();
            if (rs.next()) {
                Integer money = rs.getInt(1);
                return Optional.of(new Member(memberId, money));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw exTranslator.translate("findById", sql, e);
        } finally {
            dbConnectionUtil.close(conn, prepStmt, rs);
        }
    }

    @Override
    public Member update(Member member) {
        String sql = "update member set money = ? where member_id = ?";

        Connection conn = null;
        PreparedStatement prepStmt = null;
        try {
            conn = dbConnectionUtil.getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setInt(1, member.getMoney());
            prepStmt.setString(2, member.getMemberId());
            prepStmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            throw exTranslator.translate("update", sql, e);
        } finally {
            dbConnectionUtil.close(conn, prepStmt, null);
        }
    }

    @Override
    public void deleteById(String memberId) {
        String sql = "delete from member where member_id = ?";

        Connection conn = null;
        PreparedStatement prepStmt = null;
        try {
            conn = dbConnectionUtil.getConnection();
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, memberId);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw exTranslator.translate("deleteById", sql, e);
        } finally {
            dbConnectionUtil.close(conn, prepStmt, null);
        }
    }
}
