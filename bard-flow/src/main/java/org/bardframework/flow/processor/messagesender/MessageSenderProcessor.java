package org.bardframework.flow.processor.messagesender;

import org.bardframework.flow.exception.FlowExecutionException;
import org.bardframework.flow.processor.FormProcessorAbstract;
import org.bardframework.flow.processor.messagesender.creator.MessageCreator;
import org.bardframework.flow.processor.messagesender.sender.MessageSender;
import org.bardframework.time.LocalDateTimeJalali;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MessageSenderProcessor extends FormProcessorAbstract {

    protected final MessageCreator messageCreator;
    protected final MessageSender messageSender;
    protected final String errorMessageCode;
    protected final Executor executor = Executors.newFixedThreadPool(100);
    protected boolean failOnError = true;
    protected boolean executeInNewThread = false;
    protected DateTimeFormatter dateFormatGregorian = DateTimeFormatter.ofPattern("yyyy/dd/MM");
    protected DateTimeFormatter dateFormatterJalali = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected DateTimeFormatter dateFormatterHijrah = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm:ss");

    public MessageSenderProcessor(MessageCreator messageCreator, MessageSender messageSender, String errorMessageCode) {
        this.messageCreator = messageCreator;
        this.messageSender = messageSender;
        this.errorMessageCode = errorMessageCode;
    }

    @Override
    public final void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        String message = this.getMessageCreator().create(flowData, locale);
        LOGGER.debug("sending message [{}]", message);
        if (this.isExecuteInNewThread()) {
            this.getExecutor().execute(() -> this.sendInternal(message, flowData, locale));
        } else {
            this.sendInternal(message, flowData, locale);
        }
    }

    private void sendInternal(String message, Map<String, String> flowData, Locale locale) {
        try {
            this.beforeSend(flowData);
            this.getMessageSender().send(message, this.getArgs(flowData), locale);
            this.afterSend(flowData);
        } catch (Exception e) {
            LOGGER.error("error sending message, catching exception.", e);
            if (!this.isFailOnError()) {
                return;
            }
            throw new FlowExecutionException(List.of(errorMessageCode));
        }
    }

    protected Map<String, String> getArgs(Map<String, String> flowData) {
        Map<String, String> args = new HashMap<>(flowData);
        LocalDateTime dateTime = LocalDateTime.now();
        args.put("date", dateTime.format(this.getDateFormatGregorian()));
        args.put("jalali_date", LocalDateTimeJalali.now().format(this.getDateFormatterJalali()));
        args.put("hijrah_date", HijrahDate.now().format(this.getDateFormatterHijrah()));
        args.put("time", dateTime.format(this.getTimeFormat()));
        return args;
    }

    protected void beforeSend(Map<String, String> flowData) {
    }

    protected void afterSend(Map<String, String> flowData) {
    }

    public MessageCreator getMessageCreator() {
        return messageCreator;
    }

    public MessageSender getMessageSender() {
        return messageSender;
    }

    public Executor getExecutor() {
        return executor;
    }

    public boolean isFailOnError() {
        return failOnError;
    }

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    public String getErrorMessageCode() {
        return errorMessageCode;
    }

    public DateTimeFormatter getDateFormatGregorian() {
        return dateFormatGregorian;
    }

    public void setDateFormatGregorian(DateTimeFormatter dateFormatGregorian) {
        this.dateFormatGregorian = dateFormatGregorian;
    }

    public DateTimeFormatter getDateFormatterJalali() {
        return dateFormatterJalali;
    }

    public void setDateFormatterJalali(DateTimeFormatter dateFormatterJalali) {
        this.dateFormatterJalali = dateFormatterJalali;
    }

    public DateTimeFormatter getDateFormatterHijrah() {
        return dateFormatterHijrah;
    }

    public void setDateFormatterHijrah(DateTimeFormatter dateFormatterHijrah) {
        this.dateFormatterHijrah = dateFormatterHijrah;
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
