package org.bardframework.flow.processor.message.sender;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.flow.exception.FlowExecutionException;
import org.bardframework.flow.processor.message.creator.MessageProvider;
import org.bardframework.time.LocalDateTimeJalali;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public abstract class MessageSenderAbstract implements MessageSender {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MessageSenderAbstract.class);

    protected final Pattern canSendRegex;
    protected final String receiverFieldName;
    protected final MessageProvider messageProvider;
    protected final String errorMessageKey;
    protected Executor executor;
    protected int threadPoolSize = 100;
    protected boolean failOnError = true;
    protected boolean executeInNewThread = false;
    protected DateTimeFormatter dateFormatGregorian = DateTimeFormatter.ofPattern("yyyy/dd/MM");
    protected DateTimeFormatter dateFormatterJalali = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected DateTimeFormatter dateFormatterHijrah = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm:ss");

    public MessageSenderAbstract(String canSendRegex, String receiverFieldName, MessageProvider messageProvider, String errorMessageKey) {
        this.canSendRegex = Pattern.compile(canSendRegex);
        this.receiverFieldName = receiverFieldName;
        this.messageProvider = messageProvider;
        this.errorMessageKey = errorMessageKey;
    }

    protected abstract void send(String receiver, String message, Map<String, String> args, Locale locale) throws Exception;

    @PostConstruct
    void init() {
        if (this.isExecuteInNewThread()) {
            this.executor = Executors.newFixedThreadPool(this.getThreadPoolSize());
        }
    }

    @Override
    public final void send(Map<String, String> data, Locale locale) throws Exception {
        Map<String, String> args = new HashMap<>(data);
        String message = this.getMessageProvider().create(args, locale);
        if (StringUtils.isBlank(message)) {
            LOGGER.warn("message is empty[{}], can't send it", args);
            throw new IllegalStateException("message provider generate empty message!");
        }
        String receiver = data.get(this.getReceiverFieldName());
        if (StringUtils.isBlank(receiver)) {
            LOGGER.warn("receiver not exist for [{}], can't send message", args);
            throw new IllegalStateException("receiver not exist in args");
        }
        LOGGER.debug("sending message [{}]", message);
        if (this.isExecuteInNewThread()) {
            this.getExecutor().execute(() -> this.sendInternal(receiver, message, args, locale));
        } else {
            this.sendInternal(receiver, message, args, locale);
        }
    }

    private void sendInternal(String receiver, String message, Map<String, String> args, Locale locale) {
        try {
            this.addExtraArgs(args);
            this.send(receiver, message, args, locale);
        } catch (Exception e) {
            LOGGER.error("error sending message, catching exception.", e);
            if (!this.isFailOnError()) {
                return;
            }
            throw new FlowExecutionException(List.of(errorMessageKey));
        }
    }

    protected void addExtraArgs(Map<String, String> args) {
        LocalDateTime dateTime = LocalDateTime.now();
        args.put("date", dateTime.format(this.getDateFormatGregorian()));
        args.put("jalali_date", LocalDateTimeJalali.now().format(this.getDateFormatterJalali()));
        args.put("hijrah_date", HijrahDate.now().format(this.getDateFormatterHijrah()));
        args.put("time", dateTime.format(this.getTimeFormat()));
    }

    @Override
    public Pattern canSendRegex() {
        return canSendRegex;
    }

    public MessageProvider getMessageProvider() {
        return messageProvider;
    }

    public String getReceiverFieldName() {
        return receiverFieldName;
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

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }
}
