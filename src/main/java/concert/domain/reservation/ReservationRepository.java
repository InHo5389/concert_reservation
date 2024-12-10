package concert.domain.reservation;

import concert.domain.reservation.entity.Payment;
import concert.domain.reservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation findByConcertDateAndSeatIdAndStatus(LocalDateTime concertDate, Long seatId, ReservationStatus status);
    Reservation save(Reservation reservation);
    List<Reservation> findByStatusAndExpirationTimeBefore(ReservationStatus reservationStatus, LocalDateTime localDateTime);
    void delete(Reservation reservation);
    Optional<Reservation> findById(Long reservationId);

    Payment save(Payment payment);

    void deleteAllInBatch();

}
