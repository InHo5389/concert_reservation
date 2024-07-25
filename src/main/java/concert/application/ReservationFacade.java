package concert.application;

import concert.application.dto.ReservationDto;
import concert.common.exception.BusinessException;
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
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

        try {
            Seat seat = concertService.getSeatByOptimisticLock(seatId);

            /**
             * 생각 해보니까 낙관적 락 만을 사용하는게 아니라 이 if문을 통해 제어를 같이 한다는 생각이 들었습니다.
             * 이 if문 없이 낙관적락 버전이 바뀌면 아래 catch문으로 가고싶은데 왜 안걸리는지 잘 모르겠습니다..
             */
            if (seat.getSeatStatus() != SeatStatus.AVAILABLE) {
                throw new BusinessException("이미 예약된 좌석입니다.");
            }

            concertService.getConcertSchedule(seat.getConcertScheduleId());

            Reservation reservation = reservationService.reserveSeat(concertDate, seatId, userId, seat.getSeatPrice());
            seat.seatStatusReserved();

            return new ReservationDto(user.getUsername(), reservation.getCreatedAt(), reservation.getExpirationTime(), seat.getSeatNumber(), seat.getSeatPrice());
        } catch (OptimisticLockException e) {
            log.warn("낙관적 락 충돌 발생. 좌석 예약 실패: seatId={}", seatId);
            throw new BusinessException("이미 예약된 좌석입니다. 다른 좌석을 사용하여 주세요.");
        }
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
