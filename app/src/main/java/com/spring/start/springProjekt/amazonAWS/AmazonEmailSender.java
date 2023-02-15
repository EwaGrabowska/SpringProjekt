package com.spring.start.springProjekt.amazonAWS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

class AmazonEmailSender {

    @Value("${from.email.address}")
    private String email;
    @Value("${amazon.SES.username}")
    private String userName;
    @Value("${amazon.SES.userpassword}")
    private String userPassword;
    private static final Logger LOG = LoggerFactory.getLogger(AmazonEmailSender.class);
    @Value("${amazon.SAS.host}")
    private String HOST;

    void sendEmail(String to, String subject, String content) throws UnsupportedEncodingException, MessagingException {
        Properties props = System.getProperties();
        Session session = Session.getDefaultInstance(props);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(email, "Spring Projekt Ewa Grabowska"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setContent(content, "text/html");

        try (Transport transport = session.getTransport()) {
            transport.connect(HOST, 587, userName, userPassword);
            transport.sendMessage(msg, msg.getAllRecipients());

            LOG.debug("[INVOKED >>> AmazonEmailSender.sendEmail > user email: " + to + ", email has been successfully sent");
        } catch (Exception ex) {
            ex.printStackTrace();

            LOG.error("[INVOKED >>> AmazonEmailSender.sendEmail > user email: " + to + ", unsuccessful sending the email");
        }
    }

}
