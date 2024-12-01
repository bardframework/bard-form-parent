package org.bardframework.form.field.option;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bardframework.form.model.SelectOption;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public interface OptionDataSource {
    List<SelectOption> getOptions(Map<String, Object> args, Locale locale) throws Exception;

    @JsonIgnore
    default List<String> getIds(Map<String, Object> args, Locale locale) throws Exception {
        return this.getOptions(args, locale).stream().map(SelectOption::getId).collect(Collectors.toList());
    }
}
