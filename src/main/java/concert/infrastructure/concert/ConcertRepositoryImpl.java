package concert.infrastructure.concert;

import concert.domain.concert.Concert;
import concert.domain.concert.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    @Override
    public Optional<Concert> findById(Long concertId) {
        return concertJpaRepository.findById(concertId);
    }

    @Override
    public Concert save(Concert concert) {
        return concertJpaRepository.save(concert);
    }
}
