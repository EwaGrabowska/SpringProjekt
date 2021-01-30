package com.spring.start.springProjekt.emailSender;

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

public class AmazonEmailSender implements EmailSender{

    private static final Logger LOG = LoggerFactory.getLogger(AmazonEmailSender.class);
    static final String HOST = "email-smtp.eu-central-1.amazonaws.com";

    @Override
    public void sendEmail(Environment environment, String to, String subject, String content) throws UnsupportedEncodingException, MessagingException {

        Properties props = System.getProperties();
        Session session = Session.getDefaultInstance(props);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(environment.getProperty("email"), "Spring Projekt Ewa Grabowska"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setContent(content,"text/html");

        Transport transport = session.getTransport();

        try
        {
            transport.connect(HOST, environment.getProperty("amazon.SES.username"), environment.getProperty("amazon.SES.userpassword"));
            transport.sendMessage(msg, msg.getAllRecipients());

            LOG.debug("Email został wysłany.");

        }
        catch (Exception ex) {

            LOG.debug("Nie udało się wyśłać emaila. ");

        }
        finally
        {
            transport.close();
        }
    }

}
