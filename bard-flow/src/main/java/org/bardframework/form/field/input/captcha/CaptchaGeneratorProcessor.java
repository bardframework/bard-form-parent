package org.bardframework.form.field.input.captcha;

import org.apache.commons.io.IOUtils;
import org.bardframework.commons.captcha.GeneratedCaptcha;
import org.bardframework.flow.exception.InvalidateFlowException;
import org.bardframework.flow.processor.FormProcessorAbstract;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;

public class CaptchaGeneratorProcessor extends FormProcessorAbstract {

    private final CaptchaGenerator captchaGenerator;
    private final int maxCaptchaGenerateCount;

    public CaptchaGeneratorProcessor(CaptchaGenerator captchaGenerator, int maxCaptchaGenerateCount) {
        this.captchaGenerator = captchaGenerator;
        this.maxCaptchaGenerateCount = maxCaptchaGenerateCount;
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
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
}