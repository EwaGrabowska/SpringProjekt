package com.spring.start.springProjekt.emailSender;

import org.springframework.core.env.Environment;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailSender {
    void sendEmail(Environment environment, String to, String subject, String content) throws UnsupportedEncodingException, MessagingException;
}
