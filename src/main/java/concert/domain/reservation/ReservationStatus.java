package concert.domain.reservation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    RESERVED("예약됨"),
    PAID("결제됨"),
    EXPIRED("만료됨"),
    CANCEL("취소됨");

    private final String text;
}
