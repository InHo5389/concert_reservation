package concert.api.concert.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConcertReservationRequest {
    private LocalDateTime concertDate;
    private Long seatId;
}
