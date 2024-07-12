package concert.domain.concert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertRepository {

    Optional<Concert> findById(Long concertId);
    Concert save(Concert concert);

    List<ConcertSchedule> findAll();
    ConcertSchedule save(ConcertSchedule concertSchedule);
    ConcertSchedule findByConcertIdAndConcertDateTime(Long concertId, LocalDateTime concertDate);

    List<Seat> findByConcertScheduleIdAndSeatStatus(Long concertScheduleId,SeatStatus seatStatus);
    Seat save(Seat seat);
    Optional<Seat> findBySeatId(Long seatId);
}
