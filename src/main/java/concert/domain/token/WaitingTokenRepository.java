package concert.domain.token;

import java.util.Optional;

public interface WaitingTokenRepository {

    int countByTokenStatus(TokenStatus tokenStatus);
    WaitingToken save(WaitingToken waitingToken);
    Optional<WaitingToken> findByUserId(long userId);
}
