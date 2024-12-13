package com.example.demo;

import com.example.demo.config.DynamoDbTestRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        // Spring Boot ApplicationContext 시작
        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

        // DynamoDbTestRunner 수동 실행
        try {
            DynamoDbClient dynamoDbClient = context.getBean(DynamoDbClient.class);
            DynamoDbTestRunner testRunner = new DynamoDbTestRunner(dynamoDbClient);
            testRunner.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
