package org.bardframework.flow.form.field.input.captcha;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.commons.captcha.GeneratedCaptcha;
import org.bardframework.flow.exception.InvalidateFlowException;
import org.bardframework.flow.form.field.input.FlowInputFieldTemplate;
import org.bardframework.flow.processor.FormProcessorAbstract;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.input.CaptchaField;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CaptchaFieldTemplate extends FlowInputFieldTemplate<CaptchaField, String> {

    private static final String ANSWER_KEY = "S_captcha_answer";
    private static final String GENERATE_COUNT_KEY = "S_captcha_generate_count";

    private final CaptchaGenerator captchaGenerator;
    private final int maxCaptchaGenerateCount;
    private String refreshAction;

    public CaptchaFieldTemplate(String name, CaptchaGenerator captchaGenerator, int maxCaptchaGenerateCount) {
        super(name, false);
        this.captchaGenerator = captchaGenerator;
        this.maxCaptchaGenerateCount = maxCaptchaGenerateCount;
        this.refreshAction = "captcha-generate";
        this.setActionProcessors(Map.of(this.getRefreshAction(), List.of(new FormProcessorAbstract() {
            @Override
            public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
                generateCaptcha(flowToken, flowData, httpResponse);
            }
        })));
    }

    @Override
    public void fill(FormTemplate formTemplate, CaptchaField field, Map<String, String> values, Locale locale, HttpServletRequest httpRequest) throws Exception {
        super.fill(formTemplate, field, values, locale, httpRequest);
        field.setLength(this.getCaptchaLength());
        field.setRefreshAction(this.getRefreshAction());
        field.setRequired(true);
    }

    @Override
    public boolean isValid(String flowToken, CaptchaField field, String captcha, Map<String, String> flowData) {
        /*
            برای غیر معتبر کردن و غیر قابل استفاده شدن کپچای جاری آن را پاک می کنیم؛ پس از درخواست مجدد کاربر (واسط کاربری)، یک کپچای جدید تولید خواهد شد
            در برخی سناریوها مانند بازیابی رمز عبور؛ ممکن است کپچای صحیح وارد شود؛ ولی نام کاربری اشتباه باشد؛ در این سناریو چون کاربر در همان فرم می ماند باید کپچا تغییر کند
            به صورت کلی فقط یکبار می توان صحت یک کپچا را کنترل کرد
         */
        String captchaAnswer = flowData.remove(CaptchaFieldTemplate.ANSWER_KEY);
        if (StringUtils.isBlank(captcha)) {
            return false;
        }
        if (StringUtils.isBlank(captchaAnswer)) {
            LOGGER.debug("saved captcha answer in flow data is blank, flow token: [{}]", flowToken);
            return false;
        }
        if (!captchaAnswer.equalsIgnoreCase(captcha.replace(" ", ""))) {
            return false;
        }
        /*
            در صورتی که کپچا به درستی وارد شود؛ تعداد تلاش برای تولید کپچا را ریست می کنیم
         */
        flowData.remove(CaptchaFieldTemplate.GENERATE_COUNT_KEY);
        return true;
    }

    public void generateCaptcha(String flowToken, Map<String, String> flowData, HttpServletResponse httpResponse) throws Exception {
        int captchaGenerateCount = flowData.containsKey(CaptchaFieldTemplate.GENERATE_COUNT_KEY) ? Integer.parseInt(flowData.get(CaptchaFieldTemplate.GENERATE_COUNT_KEY)) : 0;
        if (captchaGenerateCount > maxCaptchaGenerateCount) {
            throw new InvalidateFlowException(flowToken, "max captcha generation exceed");
        }
        GeneratedCaptcha captcha = captchaGenerator.generate();
        flowData.put(CaptchaFieldTemplate.GENERATE_COUNT_KEY, String.valueOf(captchaGenerateCount + 1));
        flowData.put(CaptchaFieldTemplate.ANSWER_KEY, captcha.getValue());
        IOUtils.write("data:image/png;base64," + Base64.getEncoder().encodeToString(captcha.getImage()), httpResponse.getOutputStream(), StandardCharsets.UTF_8);
        httpResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);
        httpResponse.setStatus(HttpServletResponse.SC_OK);
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
