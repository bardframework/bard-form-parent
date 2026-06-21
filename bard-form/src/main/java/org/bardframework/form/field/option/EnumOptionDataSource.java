package org.bardframework.form.field.option;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.model.SelectOption;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Setter
@Getter
public class EnumOptionDataSource<E extends Enum<?>> extends CachableOptionDataSource {

    protected final MessageSource messageSource;
    protected final Class<? extends E> enumOptionsClass;
    protected String keyPrefix;

    public EnumOptionDataSource(Class<? extends E> enumOptionsClass, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.enumOptionsClass = enumOptionsClass;
    }

    @Override
    protected List<SelectOption> loadOptions(Map<String, Object> args, Locale locale) {
        List<SelectOption> options = new ArrayList<>();
        for (E anEnum : enumOptionsClass.getEnumConstants()) {
            options.add(new SelectOption(this.toId(anEnum, locale), this.toTitle(anEnum, locale), null));
        }
        return options;
    }

    protected String toId(E anEnum, Locale locale) {
        return anEnum.toString();
    }

    protected String toTitle(E anEnum, Locale locale) {
        String titleMessageKey = this.getMessageKey(anEnum, locale);
        return messageSource.getMessage(titleMessageKey, null, titleMessageKey, locale);
    }


    protected String toType(E anEnum, Locale locale) {
        return null;
    }

    protected String getMessageKey(E anEnum, Locale locale) {
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

}
