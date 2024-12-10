package concert.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private String username;
    private int useAmount;
    private int seatNumber;
    private int seatPrice;
    private LocalDateTime createAt;
}
