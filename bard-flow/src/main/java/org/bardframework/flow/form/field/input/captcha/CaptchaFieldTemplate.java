package org.bardframework.flow.form.field.input.captcha;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bardframework.commons.captcha.GeneratedCaptcha;
import org.bardframework.flow.form.field.input.otp.OtpFieldTemplate;
import org.bardframework.flow.form.field.input.otp.OtpGenerator;
import org.bardframework.form.field.input.CaptchaField;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class CaptchaFieldTemplate extends OtpFieldTemplate<CaptchaField, GeneratedCaptcha> {

    private static final String ANSWER_KEY = "X_ANSWER_CAPTCHA";

    public CaptchaFieldTemplate(String name, OtpGenerator<GeneratedCaptcha> otpGenerator) {
        super(name, otpGenerator, 1);
    }

    @Override
    protected void send(String flowToken, Map<String, Object> flowData, GeneratedCaptcha generatedCaptcha, Locale locale, HttpServletResponse httpResponse) throws Exception {
        flowData.put(CaptchaFieldTemplate.ANSWER_KEY, generatedCaptcha.getValue());
        IOUtils.write("data:image/png;base64," + Base64.getEncoder().encodeToString(generatedCaptcha.getImage()), httpResponse.getOutputStream(), StandardCharsets.UTF_8);
        httpResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);
        httpResponse.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected boolean isValidOtp(String flowToken, String captcha, Map<String, Object> flowData) throws Exception {
        /*
            برای غیر معتبر کردن و غیر قابل استفاده شدن کپچای جاری آن را پاک می کنیم؛ پس از درخواست مجدد کاربر (واسط کاربری)، یک کپچای جدید تولید خواهد شد
            در برخی سناریوها مانند بازیابی رمز عبور؛ ممکن است کپچای صحیح وارد شود؛ ولی نام کاربری اشتباه باشد؛ در این سناریو چون کاربر در همان فرم می ماند باید کپچا تغییر کند
            به صورت کلی فقط یکبار می توان صحت یک کپچا را کنترل کرد
         */
        Object expectedAnswer = flowData.remove(ANSWER_KEY);
        if (null == expectedAnswer) {
            log.debug("captcha answer in flow data is blank, flow token: [{}]", flowToken);
            return false;
        }
        return expectedAnswer.toString().equalsIgnoreCase(captcha);
    }

    @Override
    protected String getResendAction() {
        return "captcha-resend";
    }

    @Override
    public int getValidationOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    protected String getOtpMaxTryToResolveCountErrorMessage() {
        return "captcha.error.max.resolve.exceeded";
    }

    @Override
    protected String getMaxSendCountErrorMessage() {
        return "captcha.error.max.send.exceeded";
    }
}
