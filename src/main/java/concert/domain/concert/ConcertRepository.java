package concert.domain.concert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertRepository {

    // concert
    Optional<Concert> findById(Long concertId);
    Concert save(Concert concert);

    // concert schedule
    List<ConcertSchedule> findAll();
    ConcertSchedule save(ConcertSchedule concertSchedule);
    ConcertSchedule findByConcertIdAndConcertDateTime(Long concertId, LocalDateTime concertDate);
    List<ConcertSchedule> findByConcert(Concert concert);

    // seat
    List<Seat> findByConcertScheduleIdAndSeatStatus(Long concertScheduleId,SeatStatus seatStatus);
    Seat save(Seat seat);
    Optional<Seat> findBySeatId(Long seatId);
}
