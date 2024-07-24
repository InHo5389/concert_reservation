package concert.controller.reservation;

import concert.application.ReservationFacade;
import concert.controller.reservation.response.PaymentResponse;
import concert.controller.reservation.response.SeatReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @GetMapping("/reservation")
    public SeatReservationResponse reservation(LocalDateTime concertDate, Long seatId){
        return SeatReservationResponse.of(reservationFacade.reserveSeat(concertDate,seatId));
    }

    @PostMapping("/payment")
    public PaymentResponse pay(Long reservationId, Long userId, int amount) {
        return PaymentResponse.of(reservationFacade.pay(reservationId, userId, amount));
    }
}
