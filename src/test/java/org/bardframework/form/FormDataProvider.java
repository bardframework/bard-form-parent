package org.bardframework.form;

import com.mifmif.common.regex.Generex;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.utils.DateTimeUtils;
import org.bardframework.form.model.Form;
import org.bardframework.form.model.FormField;

import java.util.HashMap;
import java.util.Map;

public final class FormDataProvider {
    private FormDataProvider() {
        /*
            prevent instantiation
         */
    }

    public static Map<String, String> getValidData(Form form) {
        HashMap<String, String> map = new HashMap<>();
        for (FormField field : form.getFields()) {
            if (!field.isRequired() && RandomUtils.nextBoolean()) {
                continue;
            }
            if (CollectionUtils.isNotEmpty(field.getOptions())) {
                map.put(field.getName(), field.getOptions().get(RandomUtils.nextInt(0, field.getOptions().size())).getId());
            } else if (null != field.getMinValue() || null != field.getMaxValue()) {
                long minValue = null != field.getMinValue() ? field.getMinValue() : Long.MIN_VALUE;
                long maxValue = null != field.getMaxValue() ? field.getMaxValue() : Long.MAX_VALUE;
                map.put(field.getName(), String.valueOf(RandomUtils.nextLong(minValue, maxValue)));
            } else if (field.getType().startsWith("DATE")) {
                map.put(field.getName(), String.valueOf(DateTimeUtils.getNowUtcMills() - RandomUtils.nextInt()));
            } else if (StringUtils.isNotEmpty(field.getRegex())) {
                map.put(field.getName(), new Generex(field.getRegex()).random());
            } else if (null != field.getMaxLength() && field.getMaxLength() > 0) {
                map.put(field.getName(), RandomStringUtils.randomAlphanumeric(1, field.getMaxLength()));
            } else {
                map.put(field.getName(), RandomStringUtils.randomAlphanumeric(5, 100));
            }
        }
        return map;
    }

}
