package concert.infrastructure.concert;

import concert.domain.concert.Seat;
import concert.domain.concert.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<Seat,Long> {

    List<Seat> findByConcertScheduleIdAndSeatStatus(Long concertScheduleId, SeatStatus seatStatus);
    Seat save(Seat seat);
    Optional<Seat> findById(Long seatId);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from Seat s where id = :seatId")
    Optional<Seat> findByIdOptimisticLock(Long seatId);
}
