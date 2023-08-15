package inflearn.kimyounghan.jdbc.service;

import inflearn.kimyounghan.jdbc.domain.Member;
import inflearn.kimyounghan.jdbc.repository.MemberJdbcRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * 트랜잭션 AOP 이용
 */
@Slf4j
public class MemberTransactionAopService {

    private final MemberJdbcRepositoryV3 memberJdbcRepositoryV3;

    public MemberTransactionAopService(MemberJdbcRepositoryV3 memberJdbcRepositoryV3) {
        this.memberJdbcRepositoryV3 = memberJdbcRepositoryV3;
    }

    @Transactional
    public void accountTransfer(String fromId, String toId, Integer amount) throws SQLException {
        accountTransferBiz(fromId, toId, amount);
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
