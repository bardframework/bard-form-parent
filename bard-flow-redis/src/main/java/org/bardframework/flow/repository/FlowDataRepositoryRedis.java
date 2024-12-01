package org.bardframework.flow.repository;

import lombok.extern.slf4j.Slf4j;
import org.bardframework.commons.redis.DataManager;
import org.bardframework.commons.redis.DataManagerRedisImpl;
import org.bardframework.commons.utils.ReflectionUtils;
import org.bardframework.flow.FlowData;
import org.bardframework.flow.exception.InvalidateFlowException;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Slf4j
public class FlowDataRepositoryRedis<D extends FlowData> implements FlowDataRepository<D> {

    private final DataManager cache;
    private final Duration expiration;

    public FlowDataRepositoryRedis(RedisTemplate<String, Object> redisTemplate, long expirationMs) {
        this.cache = new DataManagerRedisImpl(redisTemplate);
        this.expiration = Duration.ofMillis(expirationMs);
    }

    @Override
    public boolean contains(String token) {
        return this.cache.isExist(token);
    }

    @Override
    public void put(String token, D data) {
        this.cache.putAsJson(token, data, expiration);
    }

    @Override
    public D get(String token) throws InvalidateFlowException {
        D data = this.cache.getFromJson(token, this.getDataClass());
        if (null != data) {
            return data;
        }
        throw new InvalidateFlowException(token, "no data exist for token");
    }

    @Override
    public void remove(String token) {
        this.cache.remove(token);
    }

    protected Class<D> getDataClass() {
        try {
            return ReflectionUtils.getGenericArgType(this.getClass(), 0);
        } catch (Exception e) {
            return (Class<D>) FlowData.class;
        }
    }
}