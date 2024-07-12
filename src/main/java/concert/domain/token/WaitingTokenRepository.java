package concert.domain.token;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitingTokenRepository {

    int countByTokenStatus(TokenStatus tokenStatus);
    WaitingToken save(WaitingToken waitingToken);
    Optional<WaitingToken> findByUserId(long userId);
    long findLastActiveTokenBy(long userId);
    List<WaitingToken>  findByExpiredAtBeforeAndTokenStatus(LocalDateTime now,TokenStatus status);
    List<WaitingToken> saveAll(List<WaitingToken> waitingTokenList);
    List<WaitingToken> findByTokenStatusOrderByCreatedAt(TokenStatus tokenStatus, Pageable pageable);
}
