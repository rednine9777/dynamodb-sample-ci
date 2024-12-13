package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDBConfig {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.profile:default}") // 로컬 환경에서 사용할 AWS 프로파일
    private String awsProfile;

    @Value("${spring.profiles.active:default}") // 활성 프로파일 (로컬, docker, ci 등)
    private String activeProfile;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        AwsCredentialsProvider credentialsProvider;

        if ("local".equalsIgnoreCase(activeProfile)) {
            // 로컬 환경: AWS 프로파일 사용
            credentialsProvider = ProfileCredentialsProvider.create(awsProfile);
        } else {
            // 도커/CI 환경: DefaultCredentialsProvider(OIDC 등 자동 인증)
            credentialsProvider = DefaultCredentialsProvider.create();
        }

        return DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}
