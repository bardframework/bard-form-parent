package org.bardframework.form.model;

import org.bardframework.form.template.OptionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Collator;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OptionListPropertiesFile extends ArrayList<OptionTemplate> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptionListPropertiesFile.class);

    private final String propertiesFilePath;
    private final Map<Locale, List<Option>> cache = new ConcurrentHashMap<>();
    private SortBy sortBy = SortBy.TITLE;

    public OptionListPropertiesFile(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }

    public List<Option> getOptions(Locale locale) {
        if (!cache.containsKey(locale)) {
            this.cache.put(locale, this.loadOption(locale));
        }
        return cache.get(locale);
    }

    private List<Option> loadOption(Locale locale) {
        List<Option> options = new ArrayList<>();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(propertiesFilePath, locale);
        Enumeration<String> keys = resourceBundle.getKeys();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            options.add(new Option(key, resourceBundle.getString(key)));
        }
        options.sort((option1, option2) -> {
            switch (sortBy) {
                case TITLE:
                    return Collator.getInstance(locale).compare(option1.getTitle(), option2.getTitle());
                case NATURAL_ORDER:
                    LOGGER.warn("option list order by [{}] not implemented", SortBy.NATURAL_ORDER);
                    return 0;
                default:
                    return option1.getId().compareTo(option2.getId());
            }
        });
        return options;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public enum SortBy {
        ID, TITLE, NATURAL_ORDER
    }
}
