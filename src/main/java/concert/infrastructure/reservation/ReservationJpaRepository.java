package concert.infrastructure.reservation;

import concert.domain.reservation.entity.Reservation;
import concert.domain.reservation.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationJpaRepository extends JpaRepository<Reservation,Long> {

    Reservation findByConcertDateAndSeatIdAndStatus(LocalDateTime concertDate, Long seatId, ReservationStatus status);
    Reservation save(Reservation reservation);
    List<Reservation> findByStatusAndExpirationTimeBefore(ReservationStatus reservationStatus, LocalDateTime localDateTime);
}
