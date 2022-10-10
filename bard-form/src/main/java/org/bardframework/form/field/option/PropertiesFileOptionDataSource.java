package org.bardframework.form.field.option;

import org.bardframework.commons.web.WildcardReloadableMessageSource;
import org.bardframework.form.model.SelectOption;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PropertiesFileOptionDataSource extends CachableOptionDataSource {

    private final WildcardReloadableMessageSource messageSource;

    public PropertiesFileOptionDataSource(String messageBaseName) {
        this.messageSource = new WildcardReloadableMessageSource();
        this.messageSource.setBasename(messageBaseName);
        this.messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
    }

    @Override
    protected List<SelectOption> loadOptions(Locale locale) {
        List<SelectOption> options = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : messageSource.getAllProperties(locale).entrySet()) {
            options.add(new SelectOption(entry.getKey().toString().trim(), entry.getValue().toString().trim(), null));
        }
        return options;
    }

}
