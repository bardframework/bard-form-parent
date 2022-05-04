package org.bardframework.form.model;

import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;

public interface SelectOptionsDataProvider {

    List<Option> getOptions(MessageSource messageSource, Locale locale);
}
