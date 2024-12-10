package concert.domain.concert;

import concert.domain.concert.entity.Concert;
import concert.domain.concert.entity.ConcertSchedule;
import concert.domain.concert.entity.Seat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertRepository {

    // concert
    Optional<Concert> findById(Long concertId);
    Concert save(Concert concert);

    // concert schedule
    List<ConcertSchedule> findAll();
    Optional<ConcertSchedule> findScheduleById(Long concertScheduleId);
    ConcertSchedule save(ConcertSchedule concertSchedule);
    Optional<ConcertSchedule> findByConcertIdAndConcertDateTime(Long concertId, LocalDateTime concertDate);
    List<ConcertSchedule> findByConcert(Long concertId);

    // seat
    List<Seat> findByConcertScheduleIdAndSeatStatus(Long concertScheduleId, SeatStatus seatStatus);
    Seat save(Seat seat);
    Optional<Seat> findBySeatId(Long seatId);
    Optional<Seat> findByIdOptimisticLock(Long seatId);
    Optional<Seat> findByIdPessimisticLock(Long seatId);

    void deleteAllInBatch();
    List<Seat> saveAll(List<Seat> seats);
}
