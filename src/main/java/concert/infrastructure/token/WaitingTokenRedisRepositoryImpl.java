package concert.infrastructure.token;

import concert.domain.token.WaitingTokenRedisRepository;
import concert.domain.token.entity.WaitingToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WaitingTokenRedisRepositoryImpl implements WaitingTokenRedisRepository {

    private static final String WAITING_QUEUE_KEY = "waiting:queue";
    private static final String ACTIVE_QUEUE_KEY = "active:queue";

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void addToWaitingQueue(Long userId) {
        WaitingToken token = WaitingToken.createWaitingToken(userId);
        redisTemplate.opsForZSet().add(
                WAITING_QUEUE_KEY,
                String.valueOf(userId),
                token.getScore()
        );
    }

    @Override
    public void addToActiveQueue(Long userId, LocalDateTime expiredAt) {
        WaitingToken token = WaitingToken.createActiveToken(userId, 30);
        redisTemplate.opsForSet().add(ACTIVE_QUEUE_KEY, token.formatForRedis());
    }

    @Override
    public void removeFromWaitingQueue(Long userId) {
        redisTemplate.opsForZSet().remove(WAITING_QUEUE_KEY, String.valueOf(userId));
    }

    @Override
    public void removeFromWaitingQueue(Set<String> userIds) {
        redisTemplate.opsForZSet().remove(WAITING_QUEUE_KEY, userIds.toArray());
    }

    @Override
    public void removeFromActiveQueue(Long userId) {
        Set<String> members = redisTemplate.opsForSet().members(ACTIVE_QUEUE_KEY);
        if (members != null) {
            members.stream()
                    .filter(member -> member.startsWith(userId + ":"))
                    .forEach(member -> redisTemplate.opsForSet().remove(ACTIVE_QUEUE_KEY, member));
        }
    }

    @Override
    public boolean isValidActiveToken(Long userId) {
        Set<String> members = redisTemplate.opsForSet().members(ACTIVE_QUEUE_KEY);

        if (members == null) {
            return false;
        }

        String targetUserPrefix = userId + ":";
        String userToken = null;

        // userId로 시작하는 토큰 찾기
        for (String member : members) {
            if (member.startsWith(targetUserPrefix)) {
                userToken = member;
                break;
            }
        }

        // 토큰이 없으면 false
        if (userToken == null) {
            return false;
        }

        // 시간 검증
        String[] parts = userToken.split(":");
        LocalDateTime expiredAt = LocalDateTime.parse(
                parts[1],
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        );

        boolean isValid = expiredAt.isAfter(LocalDateTime.now());

        // 만료된 경우 active 대기열에서 삭제
        if (!isValid) {
            removeFromActiveQueue(userId);
        }

        return isValid;
    }

    @Override
    public Set<String> getWaitingTokens(int count) {
        return redisTemplate.opsForZSet().range(WAITING_QUEUE_KEY, 0, count - 1);
    }

    @Override
    public Long getWaitingOrder(Long userId) {
        return redisTemplate.opsForZSet().rank(WAITING_QUEUE_KEY, String.valueOf(userId));
    }

    @Override
    public boolean existsInWaitingQueue(Long userId) {
        Double score = redisTemplate.opsForZSet().score(WAITING_QUEUE_KEY, String.valueOf(userId));
        return score != null;
    }

    @Override
    public Set<String> getAllActiveUsers() {
        return redisTemplate.opsForSet().members(ACTIVE_QUEUE_KEY)
                .stream()
                .map(member -> member.split(":")[0])
                .collect(Collectors.toSet());
    }
}
