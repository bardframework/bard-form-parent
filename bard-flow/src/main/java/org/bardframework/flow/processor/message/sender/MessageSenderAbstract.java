package org.bardframework.flow.processor.message.sender;

import org.bardframework.flow.processor.message.creator.MessageProvider;
import org.bardframework.time.LocalDateTimeJalali;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class MessageSenderAbstract implements MessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSenderAbstract.class);

    protected final MessageProvider messageProvider;
    protected final Executor executor = Executors.newFixedThreadPool(100);
    protected boolean failOnError = true;
    protected boolean executeInNewThread = false;
    protected DateTimeFormatter dateFormatGregorian = DateTimeFormatter.ofPattern("yyyy/dd/MM");
    protected DateTimeFormatter dateFormatterJalali = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected DateTimeFormatter dateFormatterHijrah = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm:ss");

    public MessageSenderAbstract(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    abstract void send(String message, Map<String, String> args, Locale locale) throws Exception;

    @Override
    public final void send(Map<String, String> data, Locale locale, HttpServletResponse httpResponse) throws Exception {
        Map<String, String> args = new HashMap<>(data);
        String message = this.getMessageProvider().create(args, locale);
        LOGGER.debug("sending message [{}]", message);
        if (this.isExecuteInNewThread()) {
            this.getExecutor().execute(() -> this.sendInternal(message, args, locale));
        } else {
            this.sendInternal(message, args, locale);
        }
    }

    private void sendInternal(String message, Map<String, String> args, Locale locale) {
        try {
            this.addExtraArgs(args);
            this.send(message, args, locale);
        } catch (Exception e) {
            LOGGER.error("error sending message, catching exception.", e);
            if (!this.isFailOnError()) {
                return;
            }
            throw new IllegalStateException(e);
        }
    }

    protected void addExtraArgs(Map<String, String> args) {
        LocalDateTime dateTime = LocalDateTime.now();
        args.put("date", dateTime.format(this.getDateFormatGregorian()));
        args.put("jalali_date", LocalDateTimeJalali.now().format(this.getDateFormatterJalali()));
        args.put("hijrah_date", HijrahDate.now().format(this.getDateFormatterHijrah()));
        args.put("time", dateTime.format(this.getTimeFormat()));
    }

    public MessageProvider getMessageProvider() {
        return messageProvider;
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
