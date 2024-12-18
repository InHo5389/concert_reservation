package concert.infrastructure.reservation;

import concert.domain.reservation.entity.Payment;
import concert.domain.reservation.entity.Reservation;
import concert.domain.reservation.ReservationRepository;
import concert.domain.reservation.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Reservation findByConcertDateAndSeatIdAndStatus(LocalDateTime concertDate, Long seatId, ReservationStatus status) {
        return reservationJpaRepository.findByConcertDateAndSeatIdAndStatus(concertDate,seatId,status);
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public List<Reservation> findByStatusAndExpirationTimeBefore(ReservationStatus reservationStatus, LocalDateTime localDateTime) {
        return reservationJpaRepository.findByStatusAndExpirationTimeBefore(reservationStatus,localDateTime);
    }

    @Override
    public void delete(Reservation reservation) {
        reservationJpaRepository.delete(reservation);
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        return reservationJpaRepository.findById(reservationId);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }


    @Override
    public void deleteAllInBatch() {
        reservationJpaRepository.deleteAllInBatch();
    }
}
