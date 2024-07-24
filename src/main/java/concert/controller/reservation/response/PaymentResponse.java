package concert.controller.reservation.response;

import concert.domain.reservation.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private String username;
    private int useAmount;
    private int seatNumber;
    private int seatPrice;
    private LocalDateTime createAt;

    public static PaymentResponse of(PaymentDto dto){
        return PaymentResponse.builder()
                .username(dto.getUsername())
                .useAmount(dto.getUseAmount())
                .seatNumber(dto.getSeatNumber())
                .seatPrice(dto.getSeatPrice())
                .build();
    }
}
