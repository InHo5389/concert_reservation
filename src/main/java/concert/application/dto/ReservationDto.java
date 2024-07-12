package concert.application.dto;

import concert.domain.concert.Seat;
import concert.domain.reservation.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationDto {
    private Reservation reservation;
    private Seat seat;
}
