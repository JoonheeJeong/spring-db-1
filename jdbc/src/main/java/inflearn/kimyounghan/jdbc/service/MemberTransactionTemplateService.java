package inflearn.kimyounghan.jdbc.service;

import inflearn.kimyounghan.jdbc.domain.Member;
import inflearn.kimyounghan.jdbc.repository.MemberJdbcRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

/**
 * 트랜잭션 템플릿 이용
 */
@Slf4j
public class MemberTransactionTemplateService {

    private final TransactionTemplate txTemplate;
    private final MemberJdbcRepositoryV3 memberJdbcRepositoryV3;

    public MemberTransactionTemplateService(PlatformTransactionManager txManager, MemberJdbcRepositoryV3 memberJdbcRepositoryV3) {
        this.txTemplate = new TransactionTemplate(txManager);
        this.memberJdbcRepositoryV3 = memberJdbcRepositoryV3;
    }

    public void accountTransfer(String fromId, String toId, Integer amount) {
        txTemplate.executeWithoutResult(status -> {
            try {
                accountTransferBiz(fromId, toId, amount);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        });
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
