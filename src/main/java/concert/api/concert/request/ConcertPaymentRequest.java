package concert.api.concert.request;

import lombok.Getter;

@Getter
public class ConcertPaymentRequest {
    private Long reservationId;
    private Long userId;
}
