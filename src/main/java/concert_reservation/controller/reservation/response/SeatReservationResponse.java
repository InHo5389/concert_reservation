package concert_reservation.controller.reservation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SeatReservationResponse {
    private String concertName;
    private LocalDateTime concertDate;
    private int seatNumber;
    private int seatPrice;
    private String seatStatus;
}
