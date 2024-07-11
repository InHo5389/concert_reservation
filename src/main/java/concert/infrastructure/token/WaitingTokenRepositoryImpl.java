package concert.infrastructure.token;

import concert.domain.token.TokenStatus;
import concert.domain.token.WaitingToken;
import concert.domain.token.WaitingTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WaitingTokenRepositoryImpl implements WaitingTokenRepository {

    private final WatingTokenJpaRepository watingTokenJpaRepository;

    @Override
    public int countByTokenStatus(TokenStatus tokenStatus) {
        return watingTokenJpaRepository.countByTokenStatus(tokenStatus);
    }

    @Override
    public WaitingToken save(WaitingToken waitingToken) {
        return watingTokenJpaRepository.save(waitingToken);
    }

    @Override
    public Optional<WaitingToken> findByUserId(long userId) {
        return watingTokenJpaRepository.findByUserId(userId);
    }

}
