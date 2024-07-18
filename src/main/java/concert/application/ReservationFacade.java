package concert.application;

import concert.application.dto.ReservationDto;
import concert.domain.concert.ConcertService;
import concert.domain.concert.Seat;
import concert.domain.reservation.Payment;
import concert.domain.reservation.PaymentDto;
import concert.domain.reservation.Reservation;
import concert.domain.reservation.ReservationService;
import concert.domain.token.jwt.WaitingTokenValidator;
import concert.domain.user.User;
import concert.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final ReservationService reservationService;
    private final ConcertService concertService;
    private final UserService userService;

    public ReservationDto reserveSeat(LocalDateTime concertDate, Long seatId) {
        Reservation reservation = reservationService.reserveSeat(concertDate, seatId);
        Seat seat = concertService.getSeat(seatId);
        return new ReservationDto(reservation, seat);
    }

    public PaymentDto pay(Long reservationId, Long userId, int amount) {
        Reservation reservation = reservationService.completeReservation(reservationId);
        userService.processPayment(userId, amount);
        Payment payment = reservationService.createPayment(reservationId, amount);

        Seat seat = concertService.getSeat(reservation.getSeatId());
        User user = userService.getUser(userId);

        return createPaymentDto(user, seat, payment);
    }

    private PaymentDto createPaymentDto(User user, Seat seat, Payment payment) {
        return PaymentDto.builder()
                .username(user.getUsername())
                .useAmount(payment.getPaymentAmount())
                .seatNumber(seat.getSeatNumber())
                .seatPrice(seat.getSeatPrice())
                .createAt(payment.getCreatedAt())
                .build();
    }
}
