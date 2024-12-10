package concert.infrastructure.concert;

import concert.domain.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConcertJpaRepository extends JpaRepository<Concert,Long> {
    Optional<Concert> findById(Long concertId);
    Concert save(Concert concert);
}
