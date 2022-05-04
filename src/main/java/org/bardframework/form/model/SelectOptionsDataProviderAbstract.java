package org.bardframework.form.model;

import org.springframework.context.MessageSource;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SelectOptionsDataProviderAbstract implements SelectOptionsDataProvider {

    private final Map<Locale, List<Option>> cache = new ConcurrentHashMap<>();
    private SortBy sortBy = SortBy.TITLE;

    public List<Option> getOptions(MessageSource messageSource, Locale locale) {
        if (!cache.containsKey(locale)) {
            List<Option> options = this.loadOption(messageSource, locale);
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
        return cache.get(locale);
    }

    protected abstract List<Option> loadOption(MessageSource messageSource, Locale locale);

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }
}