package inflearn.kimyounghan.jdbc.repository;

import inflearn.kimyounghan.jdbc.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(String memberId);

    Member update(Member member);

    void deleteById(String memberId);
}
