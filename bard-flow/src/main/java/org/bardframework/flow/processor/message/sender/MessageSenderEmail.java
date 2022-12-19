package org.bardframework.flow.processor.message.sender;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.bardframework.flow.processor.message.creator.MessageProvider;
import org.bardframework.form.field.FieldTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class MessageSenderEmail extends MessageSenderAbstract {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MessageSenderEmail.class);

    private final InternetAddress senderEmail;
    private final Properties configs;
    private final MessageProvider subjectCreator;
    private final Authenticator authenticator;

    public MessageSenderEmail(FieldTemplate<?> receiverFieldTemplate, String senderEmail, String senderUsername, String senderPassword, Properties configs, MessageProvider subjectCreator, MessageProvider emailBodyProvider, String errorMessageKey) throws AddressException {
        this(receiverFieldTemplate.getName(), senderEmail, senderUsername, senderPassword, configs, subjectCreator, emailBodyProvider, errorMessageKey);
    }

    public MessageSenderEmail(String receiverFieldName, String senderEmail, String senderUsername, String senderPassword, Properties configs, MessageProvider subjectCreator, MessageProvider emailBodyProvider, String errorMessageKey) throws AddressException {
        super(receiverFieldName, emailBodyProvider, errorMessageKey);
        this.senderEmail = InternetAddress.parse(senderEmail)[0];
        this.configs = configs;
        this.subjectCreator = subjectCreator;
        this.authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderUsername, senderPassword);
            }
        };
    }

    @Override
    protected void send(String receiver, String message, Map<String, String> args, Locale locale) throws Exception {
        Session session = Session.getInstance(configs, this.authenticator);
        if (LOGGER.isDebugEnabled()) {
            session.setDebug(true);
        }

        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(senderEmail);
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver, false));
        mimeMessage.setSubject(subjectCreator.create(args, locale), "UTF-8");
        mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
        mimeMessage.addHeader("format", "flowed");
        mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");
        mimeMessage.setContent(message, "text/html; charset=utf-8");
        Transport.send(mimeMessage);
        LOGGER.debug("email successfully sent to [{}]", receiver);
    }
}
