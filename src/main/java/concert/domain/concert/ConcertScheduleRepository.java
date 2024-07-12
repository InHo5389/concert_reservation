package concert.domain.concert;

import java.util.List;

public interface ConcertScheduleRepository {

    List<ConcertSchedule> findAll();
    ConcertSchedule save(ConcertSchedule concertSchedule);
}
