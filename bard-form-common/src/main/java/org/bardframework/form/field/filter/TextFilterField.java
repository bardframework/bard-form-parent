package org.bardframework.form.field.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.form.field.FieldType;
import org.bardframework.form.field.input.InputField;
import org.bardframework.form.model.filter.StringFilter;

@Slf4j
@Getter
@Setter
@ToString
public class TextFilterField extends InputField<StringFilter> {
    private String regex;
    private String mask;
    /**
     * این مشخصه حداقل طول داده‌ی معتبر برای فیلد را مشخص می‌کند
     * در صورتی که مشخصه‌ی required معتبر باشد به این معنی است که مقدار این فیلد حداقل 1 است
     */
    private Integer minLength;
    private Integer maxLength;

    public TextFilterField() {
    }

    protected TextFilterField(String name) {
        super(name);
    }

    @Override
    public FieldType getType() {
        return FilterFieldType.TEXT_FILTER;
    }

    @Override
    public String toString(StringFilter value) {
        //FIXME
        if (null == value) {
            return null;
        }
        return value.getContains();
    }
}