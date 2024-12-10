package concert.infrastructure.concert;

import concert.domain.concert.*;
import concert.domain.concert.entity.Concert;
import concert.domain.concert.entity.ConcertSchedule;
import concert.domain.concert.entity.Seat;
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
    public Optional<ConcertSchedule> findScheduleById(Long concertScheduleId) {
        return concertScheduleJpaRepository.findById(concertScheduleId);
    }


    @Override
    public ConcertSchedule save(ConcertSchedule concertSchedule) {
        return concertScheduleJpaRepository.save(concertSchedule);
    }

    @Override
    public Optional<ConcertSchedule> findByConcertIdAndConcertDateTime(Long concertId, LocalDateTime concertDate) {
        return concertScheduleJpaRepository.findByConcertIdAndConcertDateTime(concertId,concertDate);
    }

    @Override
    public List<ConcertSchedule> findByConcert(Long concertId) {
        return concertScheduleJpaRepository.findByConcertId(concertId);
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

    @Override
    public Optional<Seat> findByIdOptimisticLock(Long seatId) {
        return seatJpaRepository.findByIdOptimisticLock(seatId);
    }

    @Override
    public Optional<Seat> findByIdPessimisticLock(Long seatId) {
        return seatJpaRepository.findByIdPessimisticLock(seatId);
    }


    @Override
    public void deleteAllInBatch() {
        concertJpaRepository.deleteAllInBatch();
    }

    @Override
    public List<Seat> saveAll(List<Seat> seats) {
        return seatJpaRepository.saveAll(seats);
    }

}
