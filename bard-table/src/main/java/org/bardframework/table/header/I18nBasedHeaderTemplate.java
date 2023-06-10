package org.bardframework.table.header;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Slf4j
@Getter
@Setter
@ToString
public abstract class I18nBasedHeaderTemplate<M, T> extends HeaderTemplate<M, StringHeader, T> {

    private String i18nKeyPrefix;

    @Override
    protected Object format(T value, MessageSource messageSource, Locale locale) {
        String i18nKey = this.getI18nKey(value);
        if (null == i18nKey) {
            return null;
        }
        if (StringUtils.isNotBlank(i18nKeyPrefix)) {
            i18nKey = i18nKeyPrefix + "." + i18nKey;
        }
        return messageSource.getMessage(i18nKey, new Object[0], i18nKey, locale);
    }

    @Override
    protected Object formatForExport(T value, MessageSource messageSource, Locale locale) {
        String i18nKey = this.getI18nKey(value);
        if (null == i18nKey) {
            return null;
        }
        if (StringUtils.isNotBlank(i18nKeyPrefix)) {
            i18nKey = i18nKeyPrefix + "." + i18nKey;
        }
        return messageSource.getMessage(i18nKey, new Object[0], i18nKey, locale);
    }

    protected abstract String getI18nKey(T value);
}