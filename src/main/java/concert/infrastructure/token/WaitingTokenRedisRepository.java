package concert.infrastructure.token;

import concert.domain.common.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class WaitingTokenRedisRepository implements RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void zSetAdd(String key, String value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Set<String> getTopNSortedSet(String key, long n) {
        return redisTemplate.opsForZSet().range(key, 0, n - 1);
    }

    @Override
    public Long removeFromSortedSet(String key, Collection<String> values) {
        return redisTemplate.opsForZSet().remove(key, values.toArray());
    }

    @Override
    public void setAdd(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    @Override
    public Long getSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public void addAllToSet(String key, Set<String> values) {
        redisTemplate.opsForSet().add(key, values.toArray(new String[0]));
    }
}
