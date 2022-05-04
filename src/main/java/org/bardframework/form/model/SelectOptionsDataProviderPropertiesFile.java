package org.bardframework.form.model;

import org.springframework.context.MessageSource;

import java.util.*;

public class SelectOptionsDataProviderPropertiesFile extends SelectOptionsDataProviderAbstract {

    private final String propertiesFilePath;

    public SelectOptionsDataProviderPropertiesFile(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }

    @Override
    protected List<Option> loadOption(MessageSource messageSource, Locale locale) {
        List<Option> options = new ArrayList<>();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(propertiesFilePath, locale);
        Enumeration<String> keys = resourceBundle.getKeys();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            options.add(new Option(key, resourceBundle.getString(key)));
        }
        return options;
    }

}