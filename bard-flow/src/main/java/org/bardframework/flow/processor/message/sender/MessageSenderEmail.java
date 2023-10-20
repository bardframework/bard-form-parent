package org.bardframework.flow.processor.message.sender;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bardframework.flow.processor.message.creator.MessageProvider;
import org.bardframework.form.field.FieldTemplate;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Getter
public abstract class MessageSenderEmail extends MessageSenderAbstract {

    public MessageSenderEmail(FieldTemplate<?> receiverFieldTemplate, MessageProvider emailBodyProvider, String errorMessageKey) {
        this(receiverFieldTemplate.getName(), emailBodyProvider, errorMessageKey);
    }

    public MessageSenderEmail(String receiverFieldName, MessageProvider messageProvider, String errorMessageKey) {
        super(receiverFieldName, messageProvider, errorMessageKey);
    }

    @Override
    protected void send(String receiver, String message, Map<String, String> args, Locale locale) throws Exception {
        Session session = Session.getInstance(this.getConfigs(), this.getAuthenticator());
        if (log.isDebugEnabled()) {
            session.setDebug(true);
        }

        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(this.getSenderEmail());
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver, false));
        mimeMessage.setSubject(this.getSubjectCreator().create(args, locale), "UTF-8");
        mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
        mimeMessage.addHeader("format", "flowed");
        mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");
        mimeMessage.setContent(message, "text/html; charset=utf-8");
        Transport.send(mimeMessage);
        log.debug("email successfully sent to [{}]", receiver);
    }

    public abstract InternetAddress getSenderEmail();

    public abstract Properties getConfigs();

    public abstract MessageProvider getSubjectCreator();

    public abstract Authenticator getAuthenticator();
}
