package org.bardframework.flow.form.field.input.captcha;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.flow.exception.FlowDataValidationException;
import org.bardframework.flow.processor.FormProcessorAbstract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

public class CaptchaValidatorProcessor extends FormProcessorAbstract {

    private final CaptchaFieldTemplate captchaFieldTemplate;

    public CaptchaValidatorProcessor(CaptchaFieldTemplate captchaFieldTemplate) {
        this.captchaFieldTemplate = captchaFieldTemplate;
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        String captchaFieldName = captchaFieldTemplate.getName();
        String enteredCaptcha = formData.get(captchaFieldName);
        if (StringUtils.isBlank(enteredCaptcha)) {
            LOGGER.debug("entered captcha is empty, flow token: [{}]", flowToken);
            throw new FlowDataValidationException().addFiledError(captchaFieldName);
        }
        String captchaAnswer = flowData.get(CaptchaFieldTemplate.ANSWER_KEY);
        if (StringUtils.isBlank(captchaAnswer)) {
            LOGGER.debug("saved captcha answer in server is blank, flow token: [{}]", flowToken);
            throw new FlowDataValidationException().addFiledError(captchaFieldName);
        }
            /*
                برای غیر معتبر کردن کپچای جاری آن را پاک می کنیم؛ پس از درخواست مجدد کاربر (واسط کاربری)، یک کپچای جدید تولید خواهد شد
                در برخی سناریوها مانند بازیابی رمز عبور؛ ممکن است کپچای صحیح وارد شود؛ ولی نام کاربری اشتباه باشد؛ در این سناریو چون کاربر در همان فرم می ماند باید کپچا تغییر کند
                به صورت کلی فقط یکبار می توان صحت یک کپچ را کنترل کرد
             */
        flowData.remove(CaptchaFieldTemplate.ANSWER_KEY);
        if (!captchaAnswer.equalsIgnoreCase(enteredCaptcha.replaceAll(" ", ""))) {
            throw new FlowDataValidationException().addFiledError(captchaFieldName);
        }
            /*
                در صورتی که کپچا به درستی وارد شود؛ تعداد تلاش برای تولید کپچا را ریست می کنیم
             */
        flowData.remove(CaptchaFieldTemplate.GENERATE_COUNT_KEY);
    }

    @Override
    public int order() {
        return -100;
    }
}