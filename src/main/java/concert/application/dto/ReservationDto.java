package concert.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationDto {
    private String userName;
    private LocalDateTime reservationDate;
    private LocalDateTime expireDate;
    private int seatNumber;
    private int seatPrice;
}
