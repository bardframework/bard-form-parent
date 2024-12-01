package org.bardframework.form.field.option;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.model.SelectOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class I18nConstantOptionDataSource extends CachableOptionDataSource {

    protected final MessageSource messageSource;
    protected final List<SelectOption> options;
    protected String keyPrefix;

    public I18nConstantOptionDataSource(List<SelectOption> options, @Autowired MessageSource messageSource) {
        this.messageSource = messageSource;
        this.options = options;
    }

    @Override
    protected List<SelectOption> loadOptions(Map<String, Object> args, Locale locale) {
        List<SelectOption> translatedOptions = new ArrayList<>();
        for (SelectOption option : options) {
            String titleMessageKey = StringUtils.isBlank(keyPrefix) ? option.getId() : keyPrefix + "." + option.getId();
            String descriptionMessageKey = titleMessageKey + "." + "description";
            SelectOption translatedOption = new SelectOption();
            translatedOption.setId(option.getId());
            translatedOption.setTitle(messageSource.getMessage(titleMessageKey, null, titleMessageKey, locale));
            translatedOption.setDescription(messageSource.getMessage(descriptionMessageKey, null, null, locale));
            translatedOption.setDisable(option.getDisable());
            translatedOption.setType(option.getType());
            translatedOption.setIcon(option.getIcon());
            translatedOptions.add(translatedOption);
        }
        return translatedOptions;
    }
}
