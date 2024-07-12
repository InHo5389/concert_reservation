package concert.domain.token;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenStatus {
    ACTIVE("활성중"),
    WAIT("대기중"),
    EXPIRED("만료됨");

    private final String text;
}
