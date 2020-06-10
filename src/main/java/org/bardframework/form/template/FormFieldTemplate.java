package org.bardframework.form.template;

import java.util.List;

public class FormFieldTemplate {
    private final String name;
    private final String type;
    private List<OptionTemplate> options;
    private Class<? extends Enum> enumOptionsClass;

    public FormFieldTemplate(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<OptionTemplate> getOptions() {
        return options;
    }

    public void setOptions(List<OptionTemplate> options) {
        this.options = options;
    }

    public Class<? extends Enum> getEnumOptionsClass() {
        return enumOptionsClass;
    }

    public void setEnumOptionsClass(Class<? extends Enum> enumOptionsClass) {
        this.enumOptionsClass = enumOptionsClass;
    }
}
