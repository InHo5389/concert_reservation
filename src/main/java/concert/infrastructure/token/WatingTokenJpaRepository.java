package concert.infrastructure.token;

import concert.domain.token.TokenStatus;
import concert.domain.token.WaitingToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WatingTokenJpaRepository extends JpaRepository<WaitingToken,Long> {

    int countByTokenStatus(TokenStatus tokenStatus);
    Optional<WaitingToken> findByUserId(long userId);

    @Query("select wt.id from WaitingToken wt where wt.userId < :userId and wt.tokenStatus = 'ACTIVE' order by wt.createdAt desc limit 1")
    long findLastActiveTokenBy(long userId);

    List<WaitingToken> findByExpiredAtBeforeAndTokenStatus(LocalDateTime now, TokenStatus status);

    @Query("SELECT w FROM WaitingToken w WHERE w.tokenStatus = :tokenStatus ORDER BY w.createdAt")
    List<WaitingToken> findByTokenStatusOrderByCreatedAt(TokenStatus tokenStatus, Pageable pageable);
}
