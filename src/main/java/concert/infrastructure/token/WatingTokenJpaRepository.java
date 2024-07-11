package concert.infrastructure.token;

import concert.domain.token.TokenStatus;
import concert.domain.token.WaitingToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WatingTokenJpaRepository extends JpaRepository<WaitingToken,Long> {

    int countByTokenStatus(TokenStatus tokenStatus);
    Optional<WaitingToken> findByUserId(long userId);
}
