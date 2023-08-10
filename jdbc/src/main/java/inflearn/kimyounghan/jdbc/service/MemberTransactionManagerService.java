package inflearn.kimyounghan.jdbc.service;

import inflearn.kimyounghan.jdbc.domain.Member;
import inflearn.kimyounghan.jdbc.repository.MemberJdbcRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;

/**
 * 트랜잭션 매니저 이용
 */
@Slf4j
@RequiredArgsConstructor
public class MemberTransactionManagerService {

    private final PlatformTransactionManager txManager;
    private final MemberJdbcRepositoryV3 memberJdbcRepositoryV3;

    public void accountTransfer(String fromId, String toId, Integer amount) {
        TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());
        try {
            accountTransferBiz(fromId, toId, amount);
            txManager.commit(txStatus);
        } catch (Exception e) {
            txManager.rollback(txStatus);
            throw new IllegalArgumentException(e);
        }
    }

    private void accountTransferBiz(String fromId, String toId, Integer amount) throws SQLException {
        Member memberFrom = memberJdbcRepositoryV3.findById(fromId)
                .orElseThrow(() -> new RuntimeException("Member " + fromId + " not found."));
        Member memberTo = memberJdbcRepositoryV3.findById(toId)
                .orElseThrow(() -> new RuntimeException("Member " + toId + " not found."));

        validateMember(memberFrom);
        memberFrom.updateMoney(memberFrom.getMoney() - amount);
        memberJdbcRepositoryV3.update(memberFrom);

        validateMember(memberTo);
        memberTo.updateMoney(memberTo.getMoney() + amount);
        memberJdbcRepositoryV3.update(memberTo);
    }

    private void validateMember(Member member) {
        if (member.getMemberId().equals("ex"))
            throw new IllegalArgumentException("Invalid member");
    }
}
