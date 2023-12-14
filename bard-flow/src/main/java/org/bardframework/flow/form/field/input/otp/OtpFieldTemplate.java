package org.bardframework.flow.form.field.input.otp;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.flow.exception.InvalidateFlowException;
import org.bardframework.flow.exception.MaxOtpSendExceededException;
import org.bardframework.flow.form.field.input.FlowInputFieldTemplate;
import org.bardframework.flow.processor.FormProcessorAbstract;
import org.bardframework.form.FormTemplate;
import org.bardframework.form.field.input.OtpField;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Getter
@Setter
public abstract class OtpFieldTemplate<F extends OtpField, O> extends FlowInputFieldTemplate<F, String> {

    private static final String GENERATE_COUNT_KEY = "X_GENERATE_COUNT";
    private static final String RESOLVE_COUNT_KEY = "X_RESOLVE_COUNT";
    private static final String SENT_TIME_KEY = "X_SENT_TIME";
    private static final String RESEND_COUNT_KEY = "X_RESEND_COUNT";

    private final OtpGenerator<O> otpGenerator;
    private final int maxTryToResolveCount;
    private int maxSendCount = 20;
    private Integer resendIntervalSeconds;
    private Boolean canEditIdentifier;

    public OtpFieldTemplate(String name, OtpGenerator<O> otpGenerator, int maxTryToResolveCount) {
        super(name, false);
        this.otpGenerator = otpGenerator;
        this.maxTryToResolveCount = maxTryToResolveCount;
    }

    protected abstract String getResendAction();

    protected abstract void send(String flowToken, Map<String, String> flowData, O otp, Locale locale, HttpServletResponse httpResponse) throws Exception;

    protected abstract boolean isValidOtp(String flowToken, String otp, Map<String, String> flowData) throws Exception;

    @PostConstruct
    void init() {
        if (StringUtils.isNoneBlank(this.getResendAction())) {
            this.setActionProcessors(Map.of(this.getResendAction(), List.of(new FormProcessorAbstract() {
                @Override
                public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
                    resend(flowToken, flowData, locale, httpResponse);
                }
            })));
        }
    }

    @Override
    public final boolean isValid(String flowToken, F field, String value, Map<String, String> flowData) throws Exception {
        if (StringUtils.isBlank(value)) {
            log.debug("field otp [{}] is required, but it's value is empty", field.getName());
            return false;
        }
        value = value.replace(" ", "");
        if (value.length() != this.getOtpGenerator().getLength()) {
            log.debug("field [{}] length is [{}], than not equal to it's value[{}] length.", field.getName(), field.getLength(), value.length());
            return false;
        }
        if (this.isValidOtp(flowToken, value, flowData)) {
            /*
                در صورتی که مقدار ورودی صحیح باشدد؛ سایر داده هایی که در فلو نگهداری می کردیم را پاک می کنیم
            */
            flowData.remove(GENERATE_COUNT_KEY);
            flowData.remove(RESOLVE_COUNT_KEY);
            flowData.remove(SENT_TIME_KEY);
            flowData.remove(RESEND_COUNT_KEY);
            return true;
        }
        int resolveTryCount = flowData.containsKey(RESOLVE_COUNT_KEY) ? Integer.parseInt(flowData.get(RESOLVE_COUNT_KEY)) : 0;
        if (resolveTryCount >= this.getMaxTryToResolveCount()) {
            throw new InvalidateFlowException(flowToken, "invalid otp", this.getOtpMaxTryToResolveCountErrorMessage());
        }
        flowData.put(RESOLVE_COUNT_KEY, String.valueOf(resolveTryCount + 1));
        return false;
    }

    @Override
    public void fill(FormTemplate formTemplate, F field, Map<String, String> values, Locale locale) throws Exception {
        super.fill(formTemplate, field, values, locale);
        field.setLength(this.getOtpGenerator().getLength());
        field.setResendAction(this.getResendAction());
        field.setResendIntervalSeconds(this.getResendIntervalSeconds());
        field.setCanEditIdentifier(this.getCanEditIdentifier());
        field.setNumber(this.getOtpGenerator().isNumber());
        field.setRequired(true);
    }

    protected void sendInternal(String flowToken, Map<String, String> flowData, Locale locale, HttpServletResponse httpResponse) throws Exception {
        int generateCount = flowData.containsKey(GENERATE_COUNT_KEY) ? Integer.parseInt(flowData.get(GENERATE_COUNT_KEY)) : 0;
        if (generateCount > this.getMaxSendCount()) {
            throw new MaxOtpSendExceededException(flowToken, "max otp send (generate) count exceed", this.getMaxSendCountErrorMessage());
        }
        this.send(flowToken, flowData, this.getOtpGenerator().generate(), locale, httpResponse);
        flowData.put(SENT_TIME_KEY, String.valueOf(System.currentTimeMillis()));
        flowData.put(GENERATE_COUNT_KEY, String.valueOf(generateCount + 1));
    }

    public void resend(String flowToken, Map<String, String> flowData, Locale locale, HttpServletResponse httpResponse) throws Exception {
        int resendCount = flowData.containsKey(RESEND_COUNT_KEY) ? Integer.parseInt(flowData.get(RESEND_COUNT_KEY)) : 0;
        if (resendCount > this.getMaxSendCount()) {
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            log.error("flowToken[{}], max otp resend exceed", flowToken);
            return;
        }
        long sentTime = flowData.containsKey(SENT_TIME_KEY) ? Long.parseLong(flowData.get(SENT_TIME_KEY)) : 0;
        long remainSeconds = this.getResendIntervalSeconds() - ((System.currentTimeMillis() - sentTime) / 1000);
        if (remainSeconds > 0) {
            log.error("flowToken[{}], try to resend otp, before resend interval!", flowToken);
            httpResponse.getWriter().println(remainSeconds);
            httpResponse.setStatus(HttpStatus.TOO_EARLY.value());
            return;
        }
        this.sendInternal(flowToken, flowData, locale, httpResponse);
    }

    @Override
    public String toValue(String value) {
        return value;
    }

    protected abstract String getOtpMaxTryToResolveCountErrorMessage();

    protected abstract String getMaxSendCountErrorMessage();
}
