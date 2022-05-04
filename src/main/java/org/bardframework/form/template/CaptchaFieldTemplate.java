package org.bardframework.form.template;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.captcha.CaptchaService;
import org.bardframework.form.FieldType;
import org.bardframework.form.flow.exception.FlowDataValidationException;
import org.bardframework.form.flow.exception.InvalidateFlowException;
import org.bardframework.form.processor.FormProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CaptchaFieldTemplate extends FormFieldTemplate {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaFieldTemplate.class);
    private static final String ANSWER_KEY = "S_captcha_answer";
    private static final String GENERATE_COUNT_KEY = "S_captcha_generate_count";

    private final CaptchaService captchaService;
    private final CaptchaService.CaptchaType captchaType;
    private final int captchaLength;
    private final int maxCaptchaGenerateCount;
    private final String generateActionName;

    public CaptchaFieldTemplate(String name, CaptchaService.CaptchaType captchaType, int captchaLength, int maxCaptchaGenerateCount) {
        this(name, captchaType, captchaLength, maxCaptchaGenerateCount, "captcha-generate");
    }

    public CaptchaFieldTemplate(String name, CaptchaService.CaptchaType captchaType, int captchaLength, int maxCaptchaGenerateCount, String generateActionName) {
        super(name, FieldType.CAPTCHA.name(), true);
        this.captchaLength = captchaLength;
        this.maxCaptchaGenerateCount = maxCaptchaGenerateCount;
        this.captchaType = captchaType;
        this.captchaService = new CaptchaService();
        this.generateActionName = generateActionName;
        this.setPostProcessors(List.of(new CaptchaValidatorProcessor()));
        this.setActionProcessors(Map.of(this.getGenerateActionName(), List.of(new CaptchaGeneratorProcessor())));
    }

    public String getGenerateActionName() {
        return generateActionName;
    }

    private class CaptchaGeneratorProcessor implements FormProcessor {

        @Override
        public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
            int captchaGenerateCount = flowData.containsKey(GENERATE_COUNT_KEY) ? Integer.parseInt(flowData.get(GENERATE_COUNT_KEY)) : 0;
            if (captchaGenerateCount > maxCaptchaGenerateCount) {
                throw new InvalidateFlowException(flowToken, "max captcha generation exceed");
            }
            CaptchaService.Captcha captcha = captchaService.generateCaptcha(captchaType, captchaLength);
            flowData.put(GENERATE_COUNT_KEY, String.valueOf(captchaGenerateCount + 1));
            flowData.put(ANSWER_KEY, captcha.getText());
            IOUtils.write("data:image/png;base64," + Base64.getEncoder().encodeToString(captcha.getImage()), httpResponse.getOutputStream(), StandardCharsets.UTF_8);
            httpResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private class CaptchaValidatorProcessor implements FormProcessor {

        @Override
        public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
            String captchaFieldName = CaptchaFieldTemplate.this.getName();
            String enteredCaptcha = formData.get(captchaFieldName);
            if (StringUtils.isBlank(enteredCaptcha)) {
                LOGGER.debug("entered captcha is empty, flow token: [{}]", flowToken);
                throw new FlowDataValidationException().addFiledError(captchaFieldName);
            }
            String captchaAnswer = flowData.get(ANSWER_KEY);
            if (StringUtils.isBlank(captchaAnswer)) {
                LOGGER.debug("saved captcha answer in server is blank, flow token: [{}]", flowToken);
                throw new FlowDataValidationException().addFiledError(captchaFieldName);
            }
            /*
                برای غیر معتبر کردن کپچای جاری آن را پاک می کنیم؛ پس از درخواست مجدد کاربر (واسط کاربری)، یک کپچای جدید تولید خواهد شد
                در برخی سناریوها مانند بازیابی رمز عبور؛ ممکن است کپچای صحیح وارد شود؛ ولی نام کاربری اشتباه باشد؛ در این سناریو چون کاربر در همان فرم می ماند باید کپچا تغییر کند
                به صورت کلی فقط یکبار می توان صحت یک کپچ را کنترل کرد
             */
            flowData.remove(ANSWER_KEY);
            if (!captchaAnswer.equalsIgnoreCase(enteredCaptcha.replaceAll(" ", ""))) {
                throw new FlowDataValidationException().addFiledError(captchaFieldName);
            }
            /*
                در صورتی که کپچا به درستی وارد شود؛ تعداد تلاش برای تولید کپچا را ریست می کنیم
             */
            flowData.remove(GENERATE_COUNT_KEY);
        }
    }
}
