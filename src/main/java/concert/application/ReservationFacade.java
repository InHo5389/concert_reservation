package concert.application;

import concert.application.dto.ReservationDto;
import concert.domain.concert.ConcertService;
import concert.domain.concert.Seat;
import concert.domain.reservation.PaymentDto;
import concert.domain.reservation.Reservation;
import concert.domain.reservation.ReservationService;
import concert.domain.token.jwt.WaitingTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final ReservationService reservationService;
    private final WaitingTokenValidator waitingTokenValidator;
    private final ConcertService concertService;

    public ReservationDto reserveSeat(LocalDateTime concertDate, Long seatId, String jwtToken){
        waitingTokenValidator.isTokenActive(jwtToken);
        Reservation reservation = reservationService.reserveSeat(concertDate, seatId);
        Seat seat = concertService.getSeat(seatId).get();
        return new ReservationDto(reservation,seat);
    }

    public PaymentDto pay(Long reservationId, Long userId, int amount,String jwtToken){
        waitingTokenValidator.isTokenActive(jwtToken);
        return reservationService.pay(reservationId,userId,amount);
    }
}
