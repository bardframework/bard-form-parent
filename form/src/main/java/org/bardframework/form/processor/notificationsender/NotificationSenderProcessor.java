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

public abstract class NotificationSenderProcessor implements FormProcessor {
    protected final static Logger LOGGER = LoggerFactory.getLogger(NotificationSenderProcessor.class);

    protected final String messageTemplateKey;
    protected final String errorMessageCode;
    protected final MessageSource messageSource;
    protected DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected DateTimeFormatter jalaliDateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm:ss");

    public NotificationSenderProcessor(String messageTemplateKey, String errorMessageCode, @Autowired MessageSource messageSource) {
        this.messageTemplateKey = messageTemplateKey;
        this.errorMessageCode = errorMessageCode;
        this.messageSource = messageSource;
    }

    protected abstract void send(Map<String, String> flowData, String message) throws IOException;

    @Override
    public final void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        String message = this.prepareMessage(flowData, locale);
        LOGGER.debug("sending message [{}]", message);
        this.send(this.addExtraArgs(flowData), message);
    }

    protected String prepareMessage(Map<String, String> flowData, Locale locale) {
        return this.getMessageSource().getMessage(this.getMessageTemplateKey(), new Object[]{}, locale);
    }

    protected Map<String, String> addExtraArgs(Map<String, String> flowData) {
        LocalDateTimeJalali dateTimeJalali = LocalDateTimeJalali.now();
        LocalDateTime dateTime = LocalDateTime.now();
        Map<String, String> newArgs = new HashMap<>(flowData);
        newArgs.put("date", dateTime.format(this.getDateFormat()));
        newArgs.put("jalali_date", dateTimeJalali.format(this.getJalaliDateFormat()));
        newArgs.put("time", dateTime.format(this.getTimeFormat()));
        return newArgs;
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

    public DateTimeFormatter getJalaliDateFormat() {
        return jalaliDateFormat;
    }

    public void setJalaliDateFormat(DateTimeFormatter jalaliDateFormat) {
        this.jalaliDateFormat = jalaliDateFormat;
    }

    public DateTimeFormatter getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(DateTimeFormatter timeFormat) {
        this.timeFormat = timeFormat;
    }
}
