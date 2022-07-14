package org.bardframework.form.field.input.captcha;

import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.input.CaptchaField;
import org.bardframework.form.field.input.InputFieldTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CaptchaFieldTemplate extends InputFieldTemplate<CaptchaField, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaFieldTemplate.class);
    static final String ANSWER_KEY = "S_captcha_answer";
    static final String GENERATE_COUNT_KEY = "S_captcha_generate_count";

    private final CaptchaGenerator captchaGenerator;
    private String refreshAction;

    public CaptchaFieldTemplate(String name, CaptchaGenerator captchaGenerator, int maxCaptchaGenerateCount) {
        super(name, false);
        this.captchaGenerator = captchaGenerator;
        this.refreshAction = "captcha-generate";
        this.setPostProcessors(List.of(new CaptchaValidatorProcessor(this)));
        this.setActionProcessors(Map.of(this.getRefreshAction(), List.of(new CaptchaGeneratorProcessor(captchaGenerator, maxCaptchaGenerateCount))));
    }

    @Override
    public void fill(FormTemplate formTemplate, CaptchaField field, Map<String, String> args, Locale locale) throws Exception {
        super.fill(formTemplate, field, args, locale);
        field.setLength(this.getCaptchaLength());
        field.setRefreshAction(this.getRefreshAction());
    }

    @Override
    public boolean isValid(CaptchaField field, String value) {
        if (null == value) {
            if (Boolean.TRUE.equals(field.getRequired())) {
                LOGGER.debug("field [{}] is required, but it's value is empty", field.getName());
                return false;
            }
            return true;
        }
        if (value.trim().length() != field.getLength()) {
            LOGGER.debug("field [{}] length [{}] is not equal with [{}]", field.getName(), value.trim().length(), field.getLength());
            return false;
        }
        return true;
    }

    @Override
    public String toValue(String value) {
        return value;
    }

    public String getRefreshAction() {
        return refreshAction;
    }

    public void setRefreshAction(String refreshAction) {
        this.refreshAction = refreshAction;
    }

    public int getCaptchaLength() {
        return captchaGenerator.getLength();
    }
}
