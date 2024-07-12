package concert.domain.reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {

    Reservation findByConcertDateAndSeatIdAndStatus(LocalDateTime concertDate, Long seatId, ReservationStatus status);
    Reservation save(Reservation reservation);
    List<Reservation> findByStatusAndExpirationTimeBefore(ReservationStatus reservationStatus, LocalDateTime localDateTime);
    void delete(Reservation reservation);
}
