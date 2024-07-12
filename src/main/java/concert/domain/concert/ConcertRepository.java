package concert.domain.concert;

import java.util.Optional;

public interface ConcertRepository {

    Optional<Concert> findById(Long concertId);
    Concert save(Concert concert);
}
