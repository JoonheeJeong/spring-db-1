package inflearn.kimyounghan.jdbc.service;

import inflearn.kimyounghan.jdbc.domain.Member;
import inflearn.kimyounghan.jdbc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * 트랜잭션 AOP 이용
 * 레포지토리 인터페이스 의존 -> 예외 누수 해결
 */
@Slf4j
@RequiredArgsConstructor
public class MemberInterfaceDependantService {

    private final MemberRepository memberRepository;

    @Transactional
    public void accountTransfer(String fromId, String toId, Integer amount) {
        accountTransferBiz(fromId, toId, amount);
    }

    private void accountTransferBiz(String fromId, String toId, Integer amount) {
        Member memberFrom = memberRepository.findById(fromId)
                .orElseThrow(() -> new RuntimeException("Member " + fromId + " not found."));
        Member memberTo = memberRepository.findById(toId)
                .orElseThrow(() -> new RuntimeException("Member " + toId + " not found."));

        validateMember(memberFrom);
        memberFrom.updateMoney(memberFrom.getMoney() - amount);
        memberRepository.update(memberFrom);

        validateMember(memberTo);
        memberTo.updateMoney(memberTo.getMoney() + amount);
        memberRepository.update(memberTo);
    }

    private void validateMember(Member member) {
        if (member.getMemberId().equals("ex"))
            throw new IllegalArgumentException("Invalid member");
    }
}
