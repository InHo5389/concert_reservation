package concert.infrastructure.concert;

import concert.domain.concert.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule,Long> {

    List<ConcertSchedule> findAll();
    ConcertSchedule save(ConcertSchedule concertSchedule);
}
