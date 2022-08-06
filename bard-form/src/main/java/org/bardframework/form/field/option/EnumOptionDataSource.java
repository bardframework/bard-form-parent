package org.bardframework.form.field.option;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.model.SelectOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EnumOptionDataSource extends CachableOptionDataSource {

    private final MessageSource messageSource;
    private final Class<? extends Enum<?>> enumOptionsClass;
    private String keyPrefix;

    public EnumOptionDataSource(Class<? extends Enum<?>> enumOptionsClass, @Autowired MessageSource messageSource) {
        this.messageSource = messageSource;
        this.enumOptionsClass = enumOptionsClass;
    }

    @Override
    protected List<SelectOption> loadOption(Locale locale) {
        List<SelectOption> options = new ArrayList<>();
        for (Enum<?> anEnum : enumOptionsClass.getEnumConstants()) {
            String titleMessageKey = this.getMessageKey(anEnum);
            options.add(new SelectOption(anEnum.toString(), messageSource.getMessage(titleMessageKey, null, titleMessageKey, locale), null));
        }
        return options;
    }

    protected String getMessageKey(Enum<?> anEnum) {
        String prefix;
        if (null == keyPrefix) {
            prefix = anEnum.getClass().getSimpleName() + ".";
        } else if (StringUtils.isBlank(keyPrefix)) {
            prefix = "";
        } else {
            prefix = keyPrefix + ".";
        }
        return prefix + anEnum.name();
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}