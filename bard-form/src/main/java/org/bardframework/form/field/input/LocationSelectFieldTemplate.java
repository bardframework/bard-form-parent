package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;

import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class LocationSelectFieldTemplate extends InputFieldTemplateAbstract<LocationSelectField, String> {

    public LocationSelectFieldTemplate(String name) {
        super(name);
    }

    @Override
    public boolean isValid(String flowToken, LocationSelectField field, String value, Map<String, Object> flowData) {
        if (StringUtils.isBlank(value)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        return true;
    }

    @Override
    public void fill(FormTemplate formTemplate, LocationSelectField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, args, locale);
        field.setShowAddressAsTitle(FormUtils.getFieldBooleanProperty(formTemplate, this, "showAddressAsTitle", locale, args, this.getDefaultValue().getShowAddressAsTitle()));
    }
}
