package org.bardframework.form.field.option;

import org.bardframework.form.model.SelectOption;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConstantOptionDataSource implements OptionDataSource {

    private final List<SelectOption> options;

    public ConstantOptionDataSource(List<SelectOption> options) {
        this.options = options;
    }

    @Override
    public List<SelectOption> getOptions(Map<String, String> args, Locale locale) throws Exception {
        return options;
    }
}
