package org.bardframework.flow.processor.message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bardframework.flow.processor.FormProcessorAbstract;
import org.bardframework.flow.processor.message.sender.MessageSender;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MessageSenderProcessor extends FormProcessorAbstract {
    private final List<MessageSender> senders;

    public MessageSenderProcessor(List<MessageSender> senders) {
        this.senders = senders;
    }

    @Override
    public void process(String flowToken, Map<String, String> flowData, Map<String, String> formData, Locale locale, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        for (MessageSender sender : this.senders) {
            sender.send(flowData, locale);
        }
    }
}
