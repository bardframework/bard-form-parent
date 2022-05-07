package org.bardframework.form.field.option;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bardframework.form.common.field.common.SelectOption;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public abstract class CachableOptionDataSource implements OptionDataSource {

    private final Cache<Locale, List<SelectOption>> cache;
    private SortBy sortBy = SortBy.TITLE;

    protected CachableOptionDataSource() {
        this(Long.MAX_VALUE);
    }

    protected CachableOptionDataSource(long expirationMs) {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(expirationMs, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public List<SelectOption> getOptions(Locale locale) {
        List<SelectOption> options = cache.getIfPresent(locale);
        if (null == options) {
            options = this.loadOption(locale);
            options.sort((option1, option2) -> {
                switch (sortBy) {
                    case TITLE:
                        return Collator.getInstance(locale).compare(option1.getTitle(), option2.getTitle());
                    case NATURAL_ORDER:
                        return 0;
                    default:
                        return option1.getId().compareTo(option2.getId());
                }
            });
            this.cache.put(locale, options);
        }
        return options;
    }

    protected abstract List<SelectOption> loadOption(Locale locale);

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }
}