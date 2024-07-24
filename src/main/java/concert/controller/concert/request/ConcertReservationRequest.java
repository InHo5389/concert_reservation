package concert.controller.concert.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConcertReservationRequest {
    private LocalDateTime concertDate;
    private Long seatId;
}
