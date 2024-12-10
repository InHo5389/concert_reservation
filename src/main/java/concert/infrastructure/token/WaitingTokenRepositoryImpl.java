package concert.infrastructure.token;

import concert.domain.token.TokenStatus;
import concert.domain.token.entity.WaitingToken;
import concert.domain.token.WaitingTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public long findLastActiveTokenBy(long userId) {
        return watingTokenJpaRepository.findLastActiveTokenBy(userId);
    }

    @Override
    public List<WaitingToken> findByExpiredAtBeforeAndTokenStatus(LocalDateTime now, TokenStatus status) {
        return watingTokenJpaRepository.findByExpiredAtBeforeAndTokenStatus(now,status);
    }

    @Override
    public List<WaitingToken> saveAll(List<WaitingToken> waitingTokenList) {
        return watingTokenJpaRepository.saveAll(waitingTokenList);
    }

    @Override
    public List<WaitingToken> findByTokenStatusOrderByCreatedAt(TokenStatus status, Pageable pageable) {
        return watingTokenJpaRepository.findByTokenStatusOrderByCreatedAt(status,pageable);
    }

}
