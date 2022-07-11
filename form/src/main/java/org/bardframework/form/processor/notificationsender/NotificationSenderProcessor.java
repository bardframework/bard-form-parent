package org.bardframework.form.processor.notificationsender;

import org.bardframework.form.processor.FormProcessor;
import org.bardframework.time.LocalDateTimeJalali;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class NotificationSenderProcessor implements FormProcessor {
    protected final static Logger LOGGER = LoggerFactory.getLogger(NotificationSenderProcessor.class);

    protected final String messageTemplateKey;
    protected final String errorMessageCode;
    protected final boolean failOnError;
    protected final MessageSource messageSource;
    protected final Executor executor = Executors.newFixedThreadPool(100);
    protected boolean executeInNewThread;
    protected DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected DateTimeFormatter dateFormatterJalali = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm:ss");

    public NotificationSenderProcessor(String messageTemplateKey, String errorMessageCode, boolean failOnError, @Autowired MessageSource messageSource) {
        this.messageTemplateKey = messageTemplateKey;
        this.errorMessageCode = errorMessageCode;
        this.failOnError = failOnError;
        this.messageSource = messageSource;
    }

    protected abstract void send(String message, Map<String, String> args) throws IOException;

    @Override
    public final void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        String message = this.getMessageSource().getMessage(this.getMessageTemplateKey(), new Object[]{}, locale);
        LOGGER.debug("sending message [{}]", message);
        if (executeInNewThread) {
            executor.execute(() -> this.sendInternal(message, flowData));
        } else {
            this.sendInternal(message, flowData);
        }
    }

    private void sendInternal(String message, Map<String, String> flowData) {
        try {
            this.beforeSend(flowData);
            this.send(message, this.getArgs(flowData));
            this.afterSend(flowData);
        } catch (Exception e) {
            if (!failOnError) {
                LOGGER.error("error calling notification sender, failOnError is false, catching exception.", e);
                return;
            }
            throw new IllegalStateException(e);
        }
    }

    protected Map<String, String> getArgs(Map<String, String> flowData) {
        Map<String, String> args = new HashMap<>(flowData);
        LocalDateTimeJalali dateTimeJalali = LocalDateTimeJalali.now();
        LocalDateTime dateTime = LocalDateTime.now();
        args.put("date", dateTime.format(this.getDateFormat()));
        args.put("jalali_date", dateTimeJalali.format(this.getDateFormatterJalali()));
        args.put("time", dateTime.format(this.getTimeFormat()));
        return args;
    }

    protected void beforeSend(Map<String, String> flowData) {

    }

    protected void afterSend(Map<String, String> flowData) {

    }

    public String getMessageTemplateKey() {
        return messageTemplateKey;
    }

    public String getErrorMessageCode() {
        return errorMessageCode;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public DateTimeFormatter getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateTimeFormatter dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateTimeFormatter getDateFormatterJalali() {
        return dateFormatterJalali;
    }

    public void setDateFormatterJalali(DateTimeFormatter dateFormatterJalali) {
        this.dateFormatterJalali = dateFormatterJalali;
    }

    public DateTimeFormatter getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(DateTimeFormatter timeFormat) {
        this.timeFormat = timeFormat;
    }

    public boolean isExecuteInNewThread() {
        return executeInNewThread;
    }

    public void setExecuteInNewThread(boolean executeInNewThread) {
        this.executeInNewThread = executeInNewThread;
    }
}
