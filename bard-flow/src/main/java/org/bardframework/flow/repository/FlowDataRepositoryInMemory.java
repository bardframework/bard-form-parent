package org.bardframework.flow.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.flow.FlowData;
import org.bardframework.flow.exception.InvalidateFlowException;

import java.util.concurrent.TimeUnit;

@Slf4j
public class FlowDataRepositoryInMemory<D extends FlowData> implements FlowDataRepository<D> {

    private final Cache<String, D> cache;

    public FlowDataRepositoryInMemory(long expirationMs) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(expirationMs, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public boolean contains(String token) {
        return cache.getIfPresent(token) != null;
    }

    @Override
    public void put(String token, D data) {
        cache.put(token, data);
    }

    @Override
    public D get(String token) throws InvalidateFlowException {
        D data = this.cache.getIfPresent(token);
        if (null == data) {
            throw new InvalidateFlowException(token, "no data exist for token");
        }
        return data;
    }

    @Override
    public void remove(String token) {
        this.cache.invalidate(token);
    }
}
