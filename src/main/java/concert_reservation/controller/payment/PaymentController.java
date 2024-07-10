package concert_reservation.controller.payment;

import concert_reservation.controller.payment.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    public PaymentResponse pay() {
        return new PaymentResponse(1L,"임영웅 콘서트", LocalDateTime.now()
        ,1,"정인호",15000);
    }
}
