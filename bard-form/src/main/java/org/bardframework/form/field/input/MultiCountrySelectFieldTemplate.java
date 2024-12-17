package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class MultiCountrySelectFieldTemplate extends InputFieldTemplateAbstract<MultiCountrySelectField, List<String>> {

    public MultiCountrySelectFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, MultiCountrySelectField field, List<String> values, Map<String, Object> flowData) {
        if (CollectionUtils.isEmpty(values)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        List<String> countryCodes = values.stream().map(this::getCountryCode).toList();
        if (CollectionUtils.isNotEmpty(field.getAvailableCountries()) && !CollectionUtils.containsAll(field.getAvailableCountries(), countryCodes)) {
            log.debug("country codes[{}] is not match with specified available countries[{}]", countryCodes, field.getAvailableCountries());
            return false;
        }
        if (CollectionUtils.isNotEmpty(field.getExcludeCountries()) && CollectionUtils.containsAny(field.getExcludeCountries(), countryCodes)) {
            log.debug("country codes[{}] is in exclude countries[{}]", countryCodes, field.getExcludeCountries());
            return false;
        }
        return true;
    }

    protected String getCountryCode(String value) {
        return value;
    }

    @Override
    public void fill(FormTemplate formTemplate, MultiCountrySelectField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setAvailableCountries(FormUtils.getFieldListProperty(formTemplate, this.getName(), "availableCountries", locale, args, this.getDefaultValue().getAvailableCountries()));
        field.setExcludeCountries(FormUtils.getFieldListProperty(formTemplate, this.getName(), "excludeCountries", locale, args, this.defaultValue.getExcludeCountries()));
    }
}
