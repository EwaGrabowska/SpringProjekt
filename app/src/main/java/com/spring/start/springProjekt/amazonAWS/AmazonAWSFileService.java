package com.spring.start.springProjekt.amazonAWS;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.io.InputStream;

class AmazonAWSFileService {

    private final static Logger LOG = LoggerFactory.getLogger(AmazonAWSFileService.class);
    private final MessageSource messageSource;
    private final AmazonS3 client;

    AmazonAWSFileService(final MessageSource messageSource, final AmazonS3 client) {
        this.messageSource = messageSource;
        this.client = client;
    }

    void putObject(String bucketName, String keyName, InputStream inputStream, ObjectMetadata metaData) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, inputStream, metaData);
        client.putObject(putObjectRequest);
        LOG.debug("[INVOKED >>> AmazonAWSFileService.putObject > keyName: " + keyName);
    }

    void deleteObject(String buckedName, String keyName) {
        client.deleteObject(buckedName, keyName);
        LOG.debug("[INVOKED >>> AmazonAWSFileService.deleteObject > keyName: " + keyName);
    }

    S3Object downloadFile(final String buckedName, final String keyName) {
        S3Object object = client.getObject(new GetObjectRequest(buckedName, keyName));
        LOG.debug("[INVOKED >>> AmazonAWSFileService.downloadFile > keyName: " + keyName);
        return object;
    }

}
