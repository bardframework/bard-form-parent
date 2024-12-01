package org.bardframework.form.field.input;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.bardframework.commons.utils.data.LocaleCode;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MultiLocaleSelectFieldTemplate extends MultiSelectFieldTemplateAbstract<MultiLocaleSelectField> {

    public MultiLocaleSelectFieldTemplate(String name) {
        super(name, null);
    }

    @Override
    public boolean isValid(String flowToken, MultiLocaleSelectField field, List<String> values, Map<String, Object> flowData) {
        if (CollectionUtils.isEmpty(values)) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                log.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (values.size() > field.getMaxCount()) {
            log.debug("selected option count[{}] of field[{}] is greater than maximum[{}]", values.size(), field.getName(), field.getMaxCount());
            return false;
        }
        if (values.stream().anyMatch(value -> EnumUtils.getEnum(LocaleCode.class, value, null) == null)) {
            log.debug("entry locale {} is not exists in locale code", values);
            return false;
        }
        if (CollectionUtils.isNotEmpty(field.getAvailableLocales()) && field.getAvailableLocales().stream().noneMatch(values::contains)) {
            log.debug("entry locale [{}] is not match with specified available locales[{}]", values, field.getAvailableLocales());
            return false;
        }
        if (CollectionUtils.isNotEmpty(field.getExcludeLocales()) && field.getExcludeLocales().stream().anyMatch(values::contains)) {
            log.debug("entry locale [{}] is in exclude locales[{}]", values, field.getExcludeLocales());
            return false;
        }
        return true;
    }
}
