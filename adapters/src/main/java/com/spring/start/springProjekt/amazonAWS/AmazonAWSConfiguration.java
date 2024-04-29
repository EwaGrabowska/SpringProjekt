package com.spring.start.springProjekt.amazonAWS;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
class AmazonAWSConfiguration {

    private Environment environment;

    AmazonAWSConfiguration(final Environment environment) {
        this.environment = environment;
    }

    @Bean
    AmazonAWSFacade amazonAWSFacade(final Environment environment, final AmazonAWSFileService amazonFileService,
                                    final MessageSource messageSource) {
        this.environment = environment;
        return new AmazonAWSFacadeImp(environment, amazonFileService, messageSource);
    }

    @Bean
    AmazonAWSFileService amazonAWSFileService(final MessageSource messageSource, final AmazonS3 client) {
        return new AmazonAWSFileService(messageSource, client);
    }

    @Bean
    AmazonS3 getAccountClient() {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        //AWS
        String accessKey = environment.getProperty("AMAZON_S3_ACCESSKEY");
        String secretKey = environment.getProperty("AMAZON_S3_SECRETKEY");
//        //localhost
//        String accessKey = environment.getProperty("AMAZON.S3.ACCESSKEY");
//        String secretKey = environment.getProperty("AMAZON.S3.SECRETKEY");
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(clientConfig)
                .withRegion(Regions.EU_WEST_1)
                .build();

        return s3client;
    }

}
