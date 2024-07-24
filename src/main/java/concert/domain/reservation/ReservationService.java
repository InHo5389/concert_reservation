package concert.domain.reservation;

import concert.common.exception.BusinessException;
import concert.domain.concert.SeatStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private static final int FIXED_EXPIRED_MINUTES = 5;

    @Transactional
    public Reservation completeReservation(Long reservationId) {
        log.info("ReservationService completeReservation()");
        Reservation reservation = getReservation(reservationId);

        reservation.validateAndComplete();
        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(Long reservationId) {
        log.info("ReservationService getReservation()");
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException("예약된 좌석이 없습니다."));
    }

    @Transactional
    public Reservation reserveSeat(LocalDateTime concertDate, Long seatId,Long userId,int seatPrice) {
        log.info("ReservationService reserveSeat()");
        Reservation existingReservation = reservationRepository.findByConcertDateAndSeatIdAndStatus(concertDate, seatId, ReservationStatus.RESERVED);
        if (existingReservation != null) {
            log.warn("Duplicated Reservation Seat: concertDate={}, seatId={}",concertDate,seatId);
            throw new BusinessException("좌석이 이미 예약되었습니다.");
        }

        Reservation reservation = Reservation.createReservation(seatId,concertDate,FIXED_EXPIRED_MINUTES,userId,seatPrice);

        try {
            return reservationRepository.save(reservation);
        } catch (DataIntegrityViolationException e) {
            log.warn("Concurrency Reservation Seat: seatId",reservation.getSeatId());
            throw new BusinessException("동시에 같은 좌석을 예약하려는 시도가 있었습니다.");
        }
    }

    @Transactional
    public Payment createPayment(long reservationId, int amount) {
        log.info("ReservationService createPayment()");
        Payment payment = Payment.createPayment(reservationId,amount);
        return reservationRepository.save(payment);
    }
}
