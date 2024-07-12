package concert.domain.reservation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    COMPLETED("결제됨"),
    CANCEL("취소됨");

    private final String text;
}
