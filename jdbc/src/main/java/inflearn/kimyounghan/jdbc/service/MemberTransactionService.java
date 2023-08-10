package inflearn.kimyounghan.jdbc.service;

import inflearn.kimyounghan.jdbc.connection.DBConnectionUtil;
import inflearn.kimyounghan.jdbc.domain.Member;
import inflearn.kimyounghan.jdbc.repository.MemberJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 이용
 */
@Slf4j
@RequiredArgsConstructor
public class MemberTransactionService {

    private final DBConnectionUtil dbConnectionUtil = DBConnectionUtil.getInstance();
    private final MemberJdbcRepository memberJdbcRepository;

    public void accountTransfer(String fromId, String toId, Integer amount) throws SQLException {
        Connection conn = dbConnectionUtil.getConnection();
        try {
            conn.setAutoCommit(false);

            accountTransfer(conn, fromId, toId, amount);

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new IllegalArgumentException(e);
        } finally {
            release(conn);
        }
    }

    private void accountTransfer(Connection conn, String fromId, String toId, Integer amount) throws SQLException {
        Member memberFrom = memberJdbcRepository.findById(conn, fromId)
                .orElseThrow(() -> new RuntimeException("Member " + fromId + " not found."));
        Member memberTo = memberJdbcRepository.findById(conn, toId)
                .orElseThrow(() -> new RuntimeException("Member " + toId + " not found."));

        validateMember(memberFrom);
        memberFrom.updateMoney(memberFrom.getMoney() - amount);
        memberJdbcRepository.update(conn, memberFrom);

        validateMember(memberTo);
        memberTo.updateMoney(memberTo.getMoney() + amount);
        memberJdbcRepository.update(conn, memberTo);
    }

    private void validateMember(Member member) {
        if (member.getMemberId().equals("ex"))
            throw new IllegalArgumentException("Invalid member");
    }

    private static void release(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (Exception e) {
                log.error("release error", e);
            }
        }
    }
}
