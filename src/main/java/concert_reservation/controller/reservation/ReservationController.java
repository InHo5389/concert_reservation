package concert_reservation.controller.reservation;

import concert_reservation.controller.reservation.response.SeatReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    @GetMapping("/reservation")
    public SeatReservationResponse reservation(){
        return new SeatReservationResponse("임영움 콘서트", LocalDateTime.now(),1,15000,"RESERVED");
    }
}
