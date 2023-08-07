package inflearn.kimyounghan.jdbc.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Member {

    private final String memberId;
    private Integer money;

    public void updateMoney(Integer money) {
        this.money = money;
    }
}
