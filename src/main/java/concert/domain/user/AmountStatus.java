package concert.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AmountStatus {
    USE("사용"),
    CHARGE("충전");

    private final String text;
}
