package com.spring.start.springProjekt.amazonAWS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

class AmazonEmailSender {

    private String email;
    private String userName;
    private String userPassword;
    private static final Logger LOG = LoggerFactory.getLogger(AmazonEmailSender.class);
    private String host;

    AmazonEmailSender() {
    }

    void sendEmail(String to, String subject, String content) throws UnsupportedEncodingException, MessagingException {
//        this.email = System.getProperty("from_email_address");
//        this.userName = System.getProperty("amazon_SES_username");
//        this.userPassword = System.getProperty("amazon_SES_userpassword");
//        this.host = System.getProperty("amazon_SAS_host");
//
//        Properties props = System.getProperties();
//        Session session = Session.getDefaultInstance(props);
//        MimeMessage msg = new MimeMessage(session);
//        msg.setFrom(new InternetAddress(email, "Spring Projekt Ewa Grabowska"));
//        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
//        msg.setSubject(subject);
//        msg.setContent(content, "text/html");
//
//        try (Transport transport = session.getTransport()) {
//            transport.connect(host, 587, userName, userPassword);
//            transport.sendMessage(msg, msg.getAllRecipients());
//
//            LOG.debug("[INVOKED >>> AmazonEmailSender.sendEmail > user email: " + to + ", email has been successfully sent");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//
//            LOG.error("[INVOKED >>> AmazonEmailSender.sendEmail > user email: " + to + ", unsuccessful sending the email");
//        }
    }

}
