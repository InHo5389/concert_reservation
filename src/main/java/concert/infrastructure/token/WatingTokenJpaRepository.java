package concert.infrastructure.token;

import concert.domain.token.TokenStatus;
import concert.domain.token.WaitingToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WatingTokenJpaRepository extends JpaRepository<WaitingToken,Long> {

    int countByTokenStatus(TokenStatus tokenStatus);
    Optional<WaitingToken> findByUserId(long userId);

    @Query("select wt.id from WaitingToken wt where wt.userId < :userId and wt.tokenStatus = 'ACTIVE' order by wt.createdAt desc limit 1")
    long findLastActiveTokenBy(long userId);
}
