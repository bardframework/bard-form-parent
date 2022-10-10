package org.bardframework.table.header;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bardframework.crud.api.base.BaseModel;
import org.bardframework.crud.api.base.BaseRepository;
import org.springframework.context.MessageSource;

import java.io.Serializable;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public abstract class BaseModelHeaderTemplate<M extends BaseModel<I>, R extends BaseRepository<M, ?, I, U>, I extends Serializable, U> extends HeaderTemplate<StringHeader, I> {
    private final Cache<I, M> cache;
    private final R repository;

    protected BaseModelHeaderTemplate(R repository, long cacheExpirationMs) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(cacheExpirationMs, TimeUnit.MILLISECONDS)
                .build();
        this.repository = repository;
    }

    @Override
    public String format(I value, Locale locale, MessageSource messageSource) {
        U user = this.getUser();
        M model = cache.getIfPresent(value);
        if (null == model) {
            model = this.repository.get(value, user);
            if (null == model) {
                return null;
            }
            this.cache.put(value, model);
        }
        return this.getTitle(model);
    }

    protected abstract U getUser();

    protected abstract String getTitle(M model);
}
