package concert.controller.reservation;

import concert.application.ReservationFacade;
import concert.controller.reservation.response.PaymentResponse;
import concert.controller.reservation.response.SeatReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @GetMapping(value = "/reservation", headers = "Authorization" )
    public SeatReservationResponse reservation(@RequestHeader(name = "Authorization") String jwtToken, LocalDateTime concertDate, Long seatId){
        return SeatReservationResponse.of(reservationFacade.reserveSeat(concertDate,seatId,jwtToken));
    }

    @PostMapping(value = "/payments", headers = "Authorization" )
    public PaymentResponse pay(@RequestHeader(name = "Authorization") String jwtToken,Long reservationId, Long userId, int amount) {
        return PaymentResponse.of(reservationFacade.pay(reservationId, userId, amount, jwtToken));
    }
}
