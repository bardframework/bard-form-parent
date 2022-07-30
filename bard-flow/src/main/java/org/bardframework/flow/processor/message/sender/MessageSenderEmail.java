package org.bardframework.flow.processor.message.sender;

import org.apache.commons.lang3.StringUtils;
import org.bardframework.flow.processor.message.creator.MessageProvider;
import org.bardframework.form.field.FieldTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class MessageSenderEmail extends MessageSenderAbstract {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MessageSenderEmail.class);

    private final InternetAddress senderEmail;
    private final Properties configs;
    private final MessageProvider subjectCreator;
    private final FieldTemplate<?> receiverEmailFieldTemplate;
    private final Authenticator authenticator;

    public MessageSenderEmail(String senderEmail, String senderUsername, String senderPassword, Properties configs, MessageProvider subjectCreator, MessageProvider emailBodyProvider, String errorMessageKey, FieldTemplate<?> receiverEmailFieldTemplate) throws AddressException {
        super(emailBodyProvider, errorMessageKey);
        this.senderEmail = InternetAddress.parse(senderEmail)[0];
        this.configs = configs;
        this.subjectCreator = subjectCreator;
        this.receiverEmailFieldTemplate = receiverEmailFieldTemplate;
        this.authenticator = new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderUsername, senderPassword);
            }
        };
    }

    @Override
    public void send(String message, Map<String, String> args, Locale locale) throws Exception {
        String receiverEmail = args.get(this.getReceiverEmailFieldTemplate().getName());
        if (StringUtils.isBlank(receiverEmail)) {
            LOGGER.warn("receiver email not exist for [{}], can't send email", args);
            throw new IllegalStateException("receiver email not exist in args");
        }

        Session session = Session.getInstance(configs, this.authenticator);
        if (LOGGER.isDebugEnabled()) {
            session.setDebug(true);
        }

        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(senderEmail);
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail, false));
        mimeMessage.setSubject(subjectCreator.create(args, locale), "UTF-8");
        mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
        mimeMessage.addHeader("format", "flowed");
        mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");
        mimeMessage.setContent(message, "text/html; charset=utf-8");
        Transport.send(mimeMessage);
        LOGGER.debug("email successfully sent to [{}]", receiverEmail);
    }

    public FieldTemplate<?> getReceiverEmailFieldTemplate() {
        return receiverEmailFieldTemplate;
    }
}
