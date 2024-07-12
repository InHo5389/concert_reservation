package concert.infrastructure.concert;

import concert.domain.concert.Seat;
import concert.domain.concert.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<Seat,Long> {

    List<Seat> findByConcertScheduleIdAndSeatStatus(Long concertScheduleId, SeatStatus seatStatus);
    Seat save(Seat seat);
    Optional<Seat> findById(Long seatId);
}