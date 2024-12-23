package concert.domain.token;

import java.time.LocalDateTime;
import java.util.Set;

public interface WaitingTokenRedisRepository {
    void addToWaitingQueue(Long userId);
    void addToActiveQueue(Long userId, LocalDateTime expiredAt);
    void removeFromWaitingQueue(Long userId);
    void removeFromWaitingQueue(Set<String> userIds);
    void removeFromActiveQueue(Long userId);
    boolean isValidActiveToken(Long userId);
    Set<String> getWaitingTokens(int count);
    Long getWaitingOrder(Long userId);
    boolean existsInWaitingQueue(Long userId);
    Set<String> getAllActiveUsers();
}
