package concert.api.reservation;

import concert.application.ReservationFacade;
import concert.common.annotation.AuthUserId;
import concert.api.concert.request.ConcertPaymentRequest;
import concert.api.concert.request.ConcertReservationRequest;
import concert.api.reservation.response.PaymentResponse;
import concert.api.reservation.response.SeatReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @PostMapping("/reservation")
    public SeatReservationResponse reservation(@RequestBody ConcertReservationRequest request, @AuthUserId Long userId){
        return SeatReservationResponse.of(reservationFacade.reserveSeat(request.getConcertDate(), request.getSeatId(),userId));
    }

    @PostMapping("/payment")
    public PaymentResponse pay(@RequestBody ConcertPaymentRequest request) {
        return PaymentResponse.of(reservationFacade.pay(request.getReservationId(), request.getUserId()));
    }
}
