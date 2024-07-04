package concert_reservation.controller.payment.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentResponse {

    private Long concertId;
    private String concertName;
    private LocalDateTime concertDatetime;
    private int seatNumber;
    private String username;
    private int price;
}
