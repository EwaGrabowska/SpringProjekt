package com.spring.start.springProjekt.amazonAWS;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.spring.start.springProjekt.user.DTO.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import javax.mail.MessagingException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

class AmazonAWSFacadeImp implements AmazonAWSFacade {

    @Value("${amazon.EB.domain}")
    private String domain;


    private final static Logger LOG = LoggerFactory.getLogger(AmazonAWSFacadeImp.class);
    private final AmazonEmailSender amazonEmailSender = new AmazonEmailSender();
    private final AmazonAWSFileService amazonFileService;
    private final MessageSource messageSource;

    public AmazonAWSFacadeImp(final AmazonAWSFileService amazonFileService, final MessageSource messageSource) {
        this.amazonFileService = amazonFileService;
        this.messageSource = messageSource;
    }


    @Override
    public void sendEmailWithActivationLink(UserDTO user, Locale locale) throws MessagingException, UnsupportedEncodingException {
        String to = user.getEmail();
        String subject = messageSource.getMessage("user.register.email.title", null, locale);
        String content = messageSource.getMessage("user.register.email", null, locale) +
                domain + "/activatelink/" + user.getActivationCode();
        amazonEmailSender.sendEmail(to, subject, content);

        LOG.debug("[INVOKED >>> AmazonAWSFacadeImp.sendEmailWithActivationLink > user email: " + to);
    }

    @Override
    public void putObject(String bucketName, String keyName, InputStream inputStream, ObjectMetadata metaData) {
        amazonFileService.putObject(bucketName, keyName, inputStream, metaData);
        LOG.debug("[INVOKED >>> AmazonAWSFacadeImp.putObject > keyName: " + keyName);
    }

    @Override
    public void deleteObject(final String buckedName, final String keyName) {
        amazonFileService.deleteObject(buckedName, keyName);
        LOG.debug("[INVOKED >>> AmazonAWSFacadeImp.deleteObject > keyName: " + keyName);
    }

    @Override
    public S3Object downloadObject(final String buckedName, final String keyName) {
        LOG.debug("[INVOKED >>> AmazonAWSFacadeImp.downloadObject > keyName: " + keyName);
        return amazonFileService.downloadFile(buckedName, keyName);
    }
}
