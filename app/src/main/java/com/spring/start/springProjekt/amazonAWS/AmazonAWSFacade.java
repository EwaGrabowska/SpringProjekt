package com.spring.start.springProjekt.amazonAWS;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.spring.start.springProjekt.user.DTO.UserDTO;

import javax.mail.MessagingException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public interface AmazonAWSFacade {

    void sendEmailWithActivationLink(UserDTO user, Locale locale) throws MessagingException, UnsupportedEncodingException;

    void putObject(String bucketName, String keyName, InputStream inputStream, ObjectMetadata metaData);

    void deleteObject(String buckedName, String keyName);

    S3Object downloadObject(String buckedName, String keyName);


}
