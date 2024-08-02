package concert.domain.common;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface RedisRepository {

    void zSetAdd(String key,String value,double score);
    void setAdd(String key,String value);
    Long getSetSize(String key);
    void addAllToSet(String key, Set<String> values);
    Set<String> getTopNSortedSet(String key, long n);
    Long removeFromSortedSet(String key, Collection<String> values);
}
