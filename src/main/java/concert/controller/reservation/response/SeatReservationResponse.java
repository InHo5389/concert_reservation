package concert.controller.reservation.response;

import concert.application.dto.ReservationDto;
import concert.domain.concert.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatReservationResponse {
    private LocalDateTime reservationDate;
    private int seatNumber;
    private int seatPrice;
    private SeatStatus seatStatus;

    public static SeatReservationResponse of(ReservationDto dto){
        return SeatReservationResponse.builder()
                .reservationDate(dto.getReservation().getConcertDate())
                .seatNumber(dto.getSeat().getSeatNumber())
                .seatStatus(dto.getSeat().getSeatStatus())
                .seatPrice(dto.getSeat().getSeatPrice())
                .build();
    }
}
