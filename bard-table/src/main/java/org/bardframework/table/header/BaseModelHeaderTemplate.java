package org.bardframework.table.header;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bardframework.crud.api.base.BaseModel;
import org.bardframework.crud.api.base.BaseRepository;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public abstract class BaseModelHeaderTemplate<M extends BaseModel<I>, R extends BaseRepository<M, ?, I, U>, I, U> extends HeaderTemplate<M, StringHeader, I> {
    private final Cache<I, M> cache;
    private final R repository;

    protected BaseModelHeaderTemplate(R repository, long cacheExpirationMs) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(cacheExpirationMs, TimeUnit.MILLISECONDS)
                .build();
        this.repository = repository;
    }

    @Override
    public Object format(I value, MessageSource messageSource, Locale locale) {
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

    public StringHeader getEmptyHeader() {
        return new StringHeader();
    }

    protected abstract U getUser();

    protected abstract String getTitle(M model);
}
