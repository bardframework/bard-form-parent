package org.bardframework.form.field.option;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bardframework.form.common.SelectOption;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public interface OptionDataSource {
    List<SelectOption> getOptions(Locale locale) throws Exception;

    @JsonIgnore
    default List<String> getIds(Locale locale) throws Exception {
        return this.getOptions(locale).stream().map(SelectOption::getId).collect(Collectors.toList());
    }
}
