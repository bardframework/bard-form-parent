package org.bardframework.form.field.option;

import org.bardframework.form.common.field.common.SelectOption;

import java.util.List;
import java.util.Locale;

public class ConstantDataSource implements OptionDataSource {

    private final List<SelectOption> options;

    public ConstantDataSource(List<SelectOption> options) {
        this.options = options;
    }

    @Override
    public List<SelectOption> getOptions(Locale locale) {
        return options;
    }
}
