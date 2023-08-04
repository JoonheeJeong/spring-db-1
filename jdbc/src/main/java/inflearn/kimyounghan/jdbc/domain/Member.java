package inflearn.kimyounghan.jdbc.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Member {

    private final String memberId;
    private final Integer money;
}
