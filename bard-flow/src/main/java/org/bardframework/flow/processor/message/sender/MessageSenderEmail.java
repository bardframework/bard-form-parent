package org.bardframework.flow.processor.message.sender;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
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
public class MessageSenderEmail extends MessageSenderAbstract {

    private final InternetAddress senderEmail;
    private final Properties configs;
    private final MessageProvider subjectCreator;
    private final Authenticator authenticator;

    public MessageSenderEmail(FieldTemplate<?> receiverFieldTemplate, String senderEmail, Authenticator authenticator, Properties configs, MessageProvider subjectCreator, MessageProvider emailBodyProvider, String errorMessageKey) throws AddressException {
        this(receiverFieldTemplate.getName(), senderEmail, authenticator, configs, subjectCreator, emailBodyProvider, errorMessageKey);
    }

    public MessageSenderEmail(String receiverFieldName, String senderEmail, Authenticator authenticator, Properties configs, MessageProvider subjectCreator, MessageProvider emailBodyProvider, String errorMessageKey) throws AddressException {
        super(receiverFieldName, emailBodyProvider, errorMessageKey);
        this.senderEmail = InternetAddress.parse(senderEmail)[0];
        this.configs = configs;
        this.subjectCreator = subjectCreator;
        this.authenticator = authenticator;
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
}
