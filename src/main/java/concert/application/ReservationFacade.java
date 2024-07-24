package concert.application;

import concert.application.dto.ReservationDto;
import concert.domain.concert.ConcertSchedule;
import concert.domain.concert.ConcertService;
import concert.domain.concert.Seat;
import concert.domain.concert.SeatStatus;
import concert.domain.reservation.Payment;
import concert.domain.reservation.PaymentDto;
import concert.domain.reservation.Reservation;
import concert.domain.reservation.ReservationService;
import concert.domain.token.jwt.WaitingTokenValidator;
import concert.domain.user.User;
import concert.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final ReservationService reservationService;
    private final ConcertService concertService;
    private final UserService userService;

    @Transactional
    public ReservationDto reserveSeat(LocalDateTime concertDate, Long seatId, Long userId) {
        log.info("ReservationFacade reserveSeat(): concertDate={}, seatId={}", concertDate, seatId);
        User user = userService.getUser(userId);

        Seat seat = concertService.getSeat(seatId);
        concertService.getConcertSchedule(seat.getConcertScheduleId());
        Reservation reservation = reservationService.reserveSeat(concertDate, seatId, userId, seat.getSeatPrice());
        seat.seatStatusReserved();
        return new ReservationDto(user.getUsername(),reservation.getCreatedAt(),reservation.getExpirationTime(),
                seat.getSeatNumber(),seat.getSeatPrice());
    }

    public PaymentDto pay(Long reservationId, Long userId) {
        log.info("ReservationFacade pay(): reservationId={}, userId={}, amount={}", reservationId, userId);
        Reservation reservation = reservationService.completeReservation(reservationId);
        userService.processPayment(userId, reservation.getReservationAmount());
        Payment payment = reservationService.createPayment(reservationId, reservation.getReservationAmount());

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
