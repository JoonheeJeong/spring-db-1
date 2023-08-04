package inflearn.kimyounghan.jdbc.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Member {

    private final String memberId;
    private final Integer money;
}
