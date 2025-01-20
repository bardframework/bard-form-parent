package org.bardframework.form.field.option;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.Setter;
import org.bardframework.form.model.SelectOption;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
public abstract class CachableOptionDataSource implements OptionDataSource {

    private final Cache<Locale, List<SelectOption>> cache;
    private SortBy sortBy = SortBy.TITLE;

    public CachableOptionDataSource() {
        this(Long.MAX_VALUE);
    }

    public CachableOptionDataSource(long cacheExpirationMs) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(cacheExpirationMs, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public List<SelectOption> getOptions(Map<String, Object> values, Map<String, Object> args, Locale locale) {
        List<SelectOption> options = cache.getIfPresent(locale);
        if (null == options) {
            options = this.loadOptions(args, locale);
            options.sort((option1, option2) -> switch (this.getSortBy()) {
                case TITLE -> Collator.getInstance(locale).compare(option1.getTitle(), option2.getTitle());
                case NATURAL_ORDER -> 0;
                default -> option1.getId().compareTo(option2.getId());
            });
            this.cache.put(locale, options);
        }
        return options;
    }

    protected abstract List<SelectOption> loadOptions(Map<String, Object> args, Locale locale);

}
