package inflearn.kimyounghan.jdbc.service;

import inflearn.kimyounghan.jdbc.domain.Member;
import inflearn.kimyounghan.jdbc.repository.MemberJdbcRepository;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberJdbcRepository memberJdbcRepository;

    public void accountTransfer(String fromId, String toId, Integer amount) throws SQLException {
        Member memberFrom = memberJdbcRepository.findById(fromId)
                .orElseThrow(() -> new RuntimeException("Member " + fromId + " not found."));
        Member memberTo = memberJdbcRepository.findById(toId)
                .orElseThrow(() -> new RuntimeException("Member " + toId + " not found."));

        validateMember(memberFrom);
        memberFrom.updateMoney(memberFrom.getMoney() - amount);
        memberJdbcRepository.update(memberFrom);

        validateMember(memberTo);
        memberTo.updateMoney(memberTo.getMoney() + amount);
        memberJdbcRepository.update(memberTo);
    }
    private void validateMember(Member member) {
        if (member.getMemberId().equals("ex"))
            throw new IllegalArgumentException("Invalid member");
    }
}
