package org.bardframework.form.flow.repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bardframework.form.exception.InvalidateFlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class FlowDataRepositoryInMemory<D> implements FlowDataRepository<D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowDataRepositoryInMemory.class);

    private final Cache<String, D> cache;

    public FlowDataRepositoryInMemory(long expirationMs) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(expirationMs, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public void put(String token, D data) {
        cache.put(token, data);
    }

    @Override
    public D get(String token) throws InvalidateFlowException {
        D data = this.cache.getIfPresent(token);
        if (null == data) {
            throw new InvalidateFlowException("no data exist for token", token);
        }
        return data;
    }

    @Override
    public void remove(String token) {
        this.cache.invalidate(token);
    }
}
