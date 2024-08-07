package org.bardframework.flow.processor.message.sender;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bardframework.flow.exception.FlowExecutionException;
import org.bardframework.flow.processor.message.creator.MessageProvider;
import org.bardframework.time.LocalDateTimeJalali;

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

@Slf4j
@Getter
@Setter
public abstract class MessageSenderAbstract implements MessageSender {

    protected final String receiverFieldName;
    protected final MessageProvider messageProvider;
    protected final String errorMessageKey;
    protected Pattern canSendRegex;
    protected Executor executor;
    protected int threadPoolSize = 100;
    protected boolean failOnError = true;
    protected boolean executeInNewThread = false;
    protected DateTimeFormatter dateFormatGregorian = DateTimeFormatter.ofPattern("yyyy/dd/MM");
    protected DateTimeFormatter dateFormatterJalali = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected DateTimeFormatter dateFormatterHijrah = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    protected DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm:ss");

    public MessageSenderAbstract(String receiverFieldName, MessageProvider messageProvider, String errorMessageKey) {
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
            log.warn("message is empty[{}], can't send it", args);
            throw new IllegalStateException("message provider generate empty message!");
        }
        String receiver = data.get(this.getReceiverFieldName());
        if (StringUtils.isBlank(receiver)) {
            log.warn("receiver not exist for [{}], can't send message", args);
            throw new IllegalStateException("receiver not exist in args");
        }
        log.debug("sending message [{}]", message);
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
            log.error("error sending message, catching exception.", e);
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

    public void setCanSendRegex(String canSendRegex) {
        this.canSendRegex = Pattern.compile(canSendRegex);
    }

    @Override
    public Pattern canSendRegex() {
        return canSendRegex;
    }

}
