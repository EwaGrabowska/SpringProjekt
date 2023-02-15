package com.spring.start.springProjekt.amazonAWS;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.gson.Gson;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.Map;

@Configuration
class AmazonAWSConfiguration {

    @Bean
    AmazonAWSFacade amazonAWSFacade(final AmazonAWSFileService amazonFileService,
                                    final MessageSource messageSource) {
        return new AmazonAWSFacadeImp(amazonFileService, messageSource);
    }

    @Bean
    AmazonAWSFileService amazonAWSFileService(final MessageSource messageSource, final AmazonS3 client) {
        return new AmazonAWSFileService(messageSource, client);
    }

    @Bean
    AmazonS3 getAccountClient() {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        String secret = getSecret();

        Gson gson = new Gson();
        Map<String, String> secretMap = gson.fromJson(secret, Map.class);
        String accessKey = secretMap.get("accessKey");
        String secretKey = secretMap.get("secretKey");
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(clientConfig)
                .withRegion(Regions.EU_CENTRAL_1)
                .build();

        return s3client;
    }
    private String getSecret() {

        String secretName = "S3credentials";
        Region region = Region.of("eu-central-1");

        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            throw e;
        }
        return getSecretValueResponse.secretString();
    }

}
