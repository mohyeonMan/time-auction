package com.jhpark.time_auction.common.redis.util;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RedisTemplateUtil {

    private final RedisTemplate<String, Object> redis;

    public RedisTemplateUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redis = redisTemplate;
    }

    /* ===================== Key Builder (cluster hashtag) ===================== */

    /** 키 생성: namespace:{tag}:part1:part2...  예) key("game", gameId, "round", roundId, "meta") */
    public String key(String namespace, String tag, String... parts) {
        StringBuilder sb = new StringBuilder();
        sb.append(namespace).append(":").append("{").append(tag).append("}");
        for (String p : parts) sb.append(":").append(p);
        return sb.toString();
    }

    /* ===================== KV ===================== */

    public void set(String key, Object value) {
        redis.opsForValue().set(key, value);
    }

    public void set(String key, Object value, Duration ttl) {
        redis.opsForValue().set(key, value, ttl);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object v = redis.opsForValue().get(key);
        return v == null ? null : (T) v;
    }

    public boolean setIfAbsent(String key, Object value, Duration ttl) {
        Boolean b = redis.opsForValue().setIfAbsent(key, value, ttl);
        return Boolean.TRUE.equals(b);
    }

    public long incrBy(String key, long delta) {
        Long v = redis.opsForValue().increment(key, delta);
        return v == null ? 0L : v;
    }

    public boolean del(String key) {
        Boolean r = redis.delete(key);
        return Boolean.TRUE.equals(r);
    }

    public boolean expire(String key, Duration ttl) {
        Boolean r = redis.expire(key, ttl);
        return Boolean.TRUE.equals(r);
    }

    public long ttlSeconds(String key) {
        Long v = redis.getExpire(key, TimeUnit.SECONDS);
        return v == null ? -2L : v; // -2: key 없음, -1: 만료없음
    }

    /* ===================== Hash ===================== */

    public void hset(String key, String field, Object value) {
        redis.opsForHash().put(key, field, value);
    }

    public void hmset(String key, Map<String, ?> map) {
        redis.opsForHash().putAll(key, map);
    }

    @SuppressWarnings("unchecked")
    public <T> T hget(String key, String field, Class<T> type) {
        Object v = redis.opsForHash().get(key, field);
        return v == null ? null : (T) v;
    }

    public Map<Object, Object> hgetAll(String key) {
        return redis.opsForHash().entries(key);
    }

    public long hincrBy(String key, String field, long delta) {
        Long v = redis.opsForHash().increment(key, field, delta);
        return v == null ? 0L : v;
    }

    /* ===================== Set ===================== */

    public long sAdd(String key, Object... members) {
        Long r = redis.opsForSet().add(key, members);
        return r == null ? 0L : r;
    }

    public boolean sIsMember(String key, Object member) {
        Boolean r = redis.opsForSet().isMember(key, member);
        return Boolean.TRUE.equals(r);
    }

    public long sCard(String key) {
        Long r = redis.opsForSet().size(key);
        return r == null ? 0L : r;
    }

    public long sRem(String key, Object... members) {
        Long r = redis.opsForSet().remove(key, members);
        return r == null ? 0L : r;
    }

    public <T> Set<T> sMembers(String key, Class<T> type) {
        Set<Object> r = redis.opsForSet().members(key);
        if (r == null) return Collections.emptySet();

        Set<T> out = new LinkedHashSet<>(r.size());
        for (Object o : r) {
            out.add((T) o);
        }
        return out;
    }

    /* ===================== ZSet (타이머/스케줄) ===================== */

    public boolean zAdd(String key, Object member, double score) {
        Boolean r = redis.opsForZSet().add(key, member, score);
        return Boolean.TRUE.equals(r);
    }

    @SuppressWarnings("unchecked")
    public <T> Set<T> zRangeByScore(String key, double min, double max, long limit, Class<T> type) {
        Set<Object> r = redis.opsForZSet().rangeByScore(key, min, max, 0, limit);
        if (r == null || r.isEmpty()) return Collections.emptySet();
        Set<T> out = new LinkedHashSet<>(r.size());
        for (Object o : r) out.add((T) o);
        return out;
    }

    public long zRemRangeByScore(String key, double min, double max) {
        Long r = redis.opsForZSet().removeRangeByScore(key, min, max);
        return r == null ? 0L : r;
    }

    /* ===================== Pipeline ===================== */

    public List<Object> pipeline(Function<RedisTemplate<String, Object>, Void> writer) {
        return redis.executePipelined((RedisCallback<Object>) conn -> {
            writer.apply(redis);
            return null;
        });
    }

    /* ===================== 분산락 (SET NX PX) ===================== */

    /** value는 소유자 토큰(랜덤 문자열) */
    public boolean tryLock(String key, String ownerToken, Duration ttl) {
        return setIfAbsent(key, ownerToken, ttl);
    }

    /** ownerToken 일치 시에만 해제(Lua 보장) */
    public boolean unlock(String key, String ownerToken) {
        String lua = """
            if redis.call('get', KEYS[1]) == ARGV[1] then
              return redis.call('del', KEYS[1])
            else
              return 0
            end
            """;
        Long r = evalLua(lua, List.of(key), List.of(ownerToken), Long.class);
        return r != null && r > 0;
    }

    /* ===================== Lua ===================== */

    public <T> T evalLua(String script, List<String> keys, List<?> args, Class<T> returnType) {
        DefaultRedisScript<T> rs = new DefaultRedisScript<>(script, returnType);
        Object[] argv = args == null ? new Object[0] : args.toArray();
        return redis.execute(rs, keys, argv);
    }

    /** Raw eval (ReturnType 지정) */
    public @Nullable Object evalRaw(String script,
                                @Nullable List<String> keys,
                                @Nullable List<?> args,
                                ReturnType returnType) {
    return redis.execute((RedisCallback<Object>) conn -> {
        byte[] scriptBytes = script.getBytes(StandardCharsets.UTF_8);

        int numKeys = (keys == null) ? 0 : keys.size();
        int numArgs = (args == null) ? 0 : args.size();

        // keys + args 를 하나의 배열로 합치기
        byte[][] keysAndArgs = new byte[numKeys + numArgs][];

        if (numKeys > 0) {
            for (int i = 0; i < numKeys; i++) {
                keysAndArgs[i] = keys.get(i).getBytes(StandardCharsets.UTF_8);
            }
        }
        if (numArgs > 0) {
            for (int j = 0; j < numArgs; j++) {
                // Object → String → bytes (직렬화 포맷 없이 raw ARGV 전달)
                keysAndArgs[numKeys + j] = String.valueOf(args.get(j)).getBytes(StandardCharsets.UTF_8);
            }
        }

        return conn.eval(scriptBytes, returnType, numKeys, keysAndArgs);
    });
}

    /* ===================== 멱등 이벤트 도우미 ===================== */

    /** 첫 삽입이면 true(신규 이벤트), 이미 있으면 false. dedupKey에 적정 TTL 부여 권장 */
    public boolean dedupSAdd(String dedupKey, String eventId, Duration ttlIfNew) {
        Long r = redis.opsForSet().add(dedupKey, eventId);
        if (r != null && r > 0) {
            if (ttlSeconds(dedupKey) < 0) expire(dedupKey, ttlIfNew);
            return true;
        }
        return false;
    }
}
