package concert.infrastructure.concert;

import concert.domain.concert.entity.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule,Long> {

    List<ConcertSchedule> findAll();
    ConcertSchedule save(ConcertSchedule concertSchedule);
    Optional<ConcertSchedule> findByConcertIdAndConcertDateTime(Long concertId, LocalDateTime concertDate);
    List<ConcertSchedule> findByConcertId(Long concertId);
}
