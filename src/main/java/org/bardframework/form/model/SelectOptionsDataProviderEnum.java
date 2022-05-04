package org.bardframework.form.model;

import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectOptionsDataProviderEnum extends SelectOptionsDataProviderAbstract {

    private final Class<? extends Enum<?>> enumOptionsClass;
    private final String keyPrefix;

    public SelectOptionsDataProviderEnum(Class<? extends Enum<?>> enumOptionsClass) {
        this(enumOptionsClass, enumOptionsClass.getSimpleName());
    }

    public SelectOptionsDataProviderEnum(Class<? extends Enum<?>> enumOptionsClass, String keyPrefix) {
        this.enumOptionsClass = enumOptionsClass;
        this.keyPrefix = keyPrefix;
    }

    @Override
    protected List<Option> loadOption(MessageSource messageSource, Locale locale) {
        List<Option> options = new ArrayList<>();
        for (Enum<?> anEnum : enumOptionsClass.getEnumConstants()) {
            String titleMessageKey = keyPrefix + "." + anEnum.name();
            options.add(new Option(anEnum.name(), messageSource.getMessage(titleMessageKey, null, titleMessageKey, locale)));
        }
        return options;
    }
}