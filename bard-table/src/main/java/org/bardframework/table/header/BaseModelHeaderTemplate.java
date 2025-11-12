package org.bardframework.table.header;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bardframework.crud.api.base.BaseModel;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public abstract class BaseModelHeaderTemplate<M extends BaseModel<I>, I, U> extends HeaderTemplate<Object, StringHeader, I> {
    private final Cache<I, M> cache;
    private final BiFunction<I, U, M> fetchFunction;

    protected BaseModelHeaderTemplate(BiFunction<I, U, M> fetchFunction, long cacheExpirationMs) {
        this.fetchFunction = fetchFunction;
        this.cache = Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(cacheExpirationMs, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public Object format(I id, MessageSource messageSource, Locale locale) {
        U user = this.getUser();
        M model = cache.getIfPresent(id);
        if (null == model) {
            model = fetchFunction.apply(id, user);
            if (null == model) {
                return null;
            }
            this.cache.put(id, model);
        }
        return this.getTitle(model);
    }

    public StringHeader getEmptyHeader() {
        return new StringHeader();
    }

    protected abstract U getUser();

    protected abstract String getTitle(M model);
}
