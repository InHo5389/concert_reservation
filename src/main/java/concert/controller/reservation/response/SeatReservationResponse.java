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
    private String userName;
    private LocalDateTime reservationDate;
    private LocalDateTime expireDate;
    private int seatNumber;
    private int seatPrice;

    public static SeatReservationResponse of(ReservationDto dto){
        return SeatReservationResponse.builder()
                .userName(dto.getUserName())
                .reservationDate(dto.getReservationDate())
                .expireDate(dto.getExpireDate())
                .seatNumber(dto.getSeatNumber())
                .seatPrice(dto.getSeatPrice())
                .build();
    }
}
