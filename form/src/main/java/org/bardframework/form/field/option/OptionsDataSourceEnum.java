package org.bardframework.form.field.option;

import org.bardframework.form.common.field.common.SelectOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OptionsDataSourceEnum extends CachableOptionDataSource {

    private final MessageSource messageSource;
    private final Class<? extends Enum<?>> enumOptionsClass;
    private String keyPrefix;

    public OptionsDataSourceEnum(Class<? extends Enum<?>> enumOptionsClass, @Autowired MessageSource messageSource) {
        this.messageSource = messageSource;
        this.enumOptionsClass = enumOptionsClass;
    }

    @Override
    protected List<SelectOption> loadOption(Locale locale) {
        List<SelectOption> options = new ArrayList<>();
        for (Enum<?> anEnum : enumOptionsClass.getEnumConstants()) {
            String titleMessageKey = keyPrefix + "." + anEnum.name();
            options.add(new SelectOption(anEnum.name(), messageSource.getMessage(titleMessageKey, null, titleMessageKey, locale), null));
        }
        return options;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}