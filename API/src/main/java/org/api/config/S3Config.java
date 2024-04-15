package org.api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Getter
@Setter
public class S3Config {

    @Configuration
    @Profile("dev")
    @RequiredArgsConstructor
    public static class DevS3Config {
        @Value("${cloud.aws.credentials.accessKey}")
        private String accessKey;
        @Value("${cloud.aws.credentials.secretKey}")
        private String secretKey;
        @Value("${cloud.aws.region.static}")
        private String region;

        @Bean
        public AmazonS3Client amazonS3Client() {
            BasicAWSCredentials basicAWSCredentials = new  BasicAWSCredentials(accessKey, secretKey);
            return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                    .build();
        }
    }

    @Configuration
    @Profile("prod")
    @RequiredArgsConstructor
    public static class ProdS3Config {
        @Value("${AWS_ACCESS_KEY_ID}")
        private String accessKey;

        @Value("${AWS_SECRET_ACCESS_KEY}")
        private String secretKey;

        @Bean
        public AmazonS3Client amazonS3Client() {
            BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
            String region = "ap-northeast-2";
            return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                    .build();
        }
    }


}
