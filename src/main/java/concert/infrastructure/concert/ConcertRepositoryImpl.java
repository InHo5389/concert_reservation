package concert.infrastructure.concert;

import concert.domain.concert.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final SeatJpaRepository seatJpaRepository;

    @Override
    public Optional<Concert> findById(Long concertId) {
        return concertJpaRepository.findById(concertId);
    }

    @Override
    public Concert save(Concert concert) {
        return concertJpaRepository.save(concert);
    }

    @Override
    public List<ConcertSchedule> findAll() {
        return concertScheduleJpaRepository.findAll();
    }

    @Override
    public ConcertSchedule save(ConcertSchedule concertSchedule) {
        return concertScheduleJpaRepository.save(concertSchedule);
    }

    @Override
    public ConcertSchedule findByConcertIdAndConcertDateTime(Long concertId, LocalDateTime concertDate) {
        return concertScheduleJpaRepository.findByConcertIdAndConcertDateTime(concertId,concertDate);
    }

    @Override
    public List<Seat> findByConcertScheduleIdAndSeatStatus(Long concertScheduleId, SeatStatus seatStatus) {
        return seatJpaRepository.findByConcertScheduleIdAndSeatStatus(concertScheduleId,seatStatus);
    }

    @Override
    public Seat save(Seat seat) {
        return seatJpaRepository.save(seat);
    }

    @Override
    public Optional<Seat> findBySeatId(Long seatId) {
        return seatJpaRepository.findById(seatId);
    }
}
