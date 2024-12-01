package org.bardframework.flow.form.field.input.otp.sendsms;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.flow.exception.FlowExecutionException;
import org.bardframework.flow.exception.InvalidateFlowException;
import org.bardframework.flow.form.field.input.FlowInputFieldTemplate;
import org.bardframework.flow.form.field.input.otp.OtpGenerator;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.FormUtils;
import org.bardframework.form.exception.FormDataValidationException;
import org.bardframework.form.field.FieldTemplate;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

@Getter
@Setter
public class SsotpFieldTemplate extends FlowInputFieldTemplate<SsotpField, String> {
    private static final String RESOLVE_COUNT_KEY = "X_RESOLVE_COUNT";
    private static final String ANSWER_KEY = "X_GENERATED_SSOTP";

    private final OtpGenerator<String> otpGenerator;
    private final int maxTryToResolveCount;
    private final SsotpMessageReceiver ssotpMessageReceiver;
    private final FieldTemplate<?> identifierFieldTemplate;

    public SsotpFieldTemplate(String name, OtpGenerator<String> otpGenerator, int maxTryToResolveCount, SsotpMessageReceiver ssotpMessageReceiver, FieldTemplate<?> identifierFieldTemplate) {
        super(name);
        this.otpGenerator = otpGenerator;
        this.maxTryToResolveCount = maxTryToResolveCount;
        this.ssotpMessageReceiver = ssotpMessageReceiver;
        this.identifierFieldTemplate = identifierFieldTemplate;
    }

    @Override
    public void preProcess(String flowToken, Map<String, Object> flowData, Locale locale, HttpServletResponse httpResponse) throws Exception {
        String otp = this.getOtpGenerator().generate();
        flowData.put(ANSWER_KEY, otp);
    }

    @Override
    public void validate(String flowToken, FormTemplate formTemplate, Map<String, Object> flowData, Map<String, Object> formData, Locale locale, FormDataValidationException ex) throws Exception {
        Object identifier = flowData.get(this.identifierFieldTemplate.getName());
        Object expectedAnswer = flowData.get(ANSWER_KEY);
        String receivedOtp = ssotpMessageReceiver.remove(identifier.toString());
        if (null != expectedAnswer && expectedAnswer.equals(receivedOtp)) {
            flowData.remove(ANSWER_KEY);
            flowData.remove(RESOLVE_COUNT_KEY);
            return;
        }
        int resolveTryCount = flowData.containsKey(RESOLVE_COUNT_KEY) ? Integer.parseInt(flowData.get(RESOLVE_COUNT_KEY).toString()) : 0;
        if (resolveTryCount >= this.getMaxTryToResolveCount()) {
            throw new InvalidateFlowException(flowToken, "too many try to resolve ssotp, terminating flow...");
        }
        flowData.put(RESOLVE_COUNT_KEY, String.valueOf(resolveTryCount + 1));
        if (StringUtils.isBlank(receivedOtp)) {
            throw new FlowExecutionException(Collections.singletonList("field.ssotp.error.notReceived"));
        }
        throw new FlowExecutionException(Collections.singletonList("field.ssotp.error.mismatch"));
    }

    @Override
    public void fill(FormTemplate formTemplate, SsotpField field, Map<String, Object> values, Map<String, Object> args, Locale locale) throws Exception {
        field.setTitle(FormUtils.getFieldStringProperty(formTemplate, this, "title", locale, args, this.getDefaultValue().getTitle()));
        field.setDescription(FormUtils.getFieldStringProperty(formTemplate, this, "description", locale, args, this.getDefaultValue().getDescription()));
        field.setNumber(this.getOtpGenerator().isNumber());
        field.setOtp(args.get(ANSWER_KEY).toString());
    }

    @Override
    public boolean isValid(String flowToken, SsotpField field, String value, Map<String, Object> flowData) {
        return true;
    }
}
