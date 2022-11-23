package com.spring.start.springProjekt.amazonAWS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

class AmazonEmailSender {

    private static final Logger LOG = LoggerFactory.getLogger(AmazonEmailSender.class);
    static final String HOST = "email-smtp.eu-central-1.amazonaws.com";

    void sendEmail(Environment environment, String to, String subject, String content) throws UnsupportedEncodingException, MessagingException {
        Properties props = System.getProperties();
        Session session = Session.getDefaultInstance(props);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(environment.getProperty("from.email.address"), "Spring Projekt Ewa Grabowska"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setContent(content, "text/html");

        try (Transport transport = session.getTransport()) {
            transport.connect(HOST, 587, environment.getProperty("amazon.SES.username"), environment.getProperty("amazon.SES.userpassword"));
            transport.sendMessage(msg, msg.getAllRecipients());

            LOG.debug("[INVOKED >>> AmazonEmailSender.sendEmail > user email: " + to + ", email has been successfully sent");
        } catch (Exception ex) {
            ex.printStackTrace();

            LOG.error("[INVOKED >>> AmazonEmailSender.sendEmail > user email: " + to + ", unsuccessful sending the email");
        }
    }

}
