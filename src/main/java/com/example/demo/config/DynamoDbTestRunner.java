package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Component
public class DynamoDbTestRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DynamoDbTestRunner.class);
    private final DynamoDbClient dynamoDbClient;
    private static boolean hasRun = false; // 중복 실행 방지 플래그

    public DynamoDbTestRunner(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public synchronized void run(String... args) { // synchronized로 동시 실행 방지
        if (hasRun) {
            logger.warn("DynamoDbTestRunner has already been executed. Skipping...");
            return;
        }

        hasRun = true; // 실행 플래그 설정

        StringBuilder logBuilder = new StringBuilder();

        try {
            // 연결 성공 여부 출력
            logBuilder.append("\n\n==============================================\n");
            logBuilder.append("        DynamoDB Connection: SUCCESS          \n");
            logBuilder.append("==============================================\n\n");

            // 리전 정보 가져오기
            String region = dynamoDbClient.serviceClientConfiguration().region().toString();

            // 프로파일 이름 가져오기
            String profileName = System.getenv("AWS_PROFILE");
            if (profileName == null || profileName.isEmpty()) {
                profileName = "default";
            }

            // AWS 정보 출력
            logBuilder.append("AWS Information:\n");
            logBuilder.append("-----------------------------------------------\n");
            logBuilder.append(String.format("| Profile       | %-30s |\n", profileName));
            logBuilder.append(String.format("| Region        | %-30s |\n", region));
            logBuilder.append("-----------------------------------------------\n\n");

            // DynamoDB 테이블 목록 출력
            logBuilder.append("DynamoDB Tables:\n");
            logBuilder.append("-----------------------------------------------\n");
            dynamoDbClient.listTables().tableNames().forEach(table -> 
                logBuilder.append(String.format("| Table Name    | %-30s |\n", table))
            );
            logBuilder.append("-----------------------------------------------\n\n");

        } catch (Exception e) {
            // 연결 실패 시
            logBuilder.setLength(0); // 기존 로그 초기화
            logBuilder.append("\n\n==============================================\n");
            logBuilder.append("        DynamoDB Connection: FAILED           \n");
            logBuilder.append("==============================================\n\n");
            logBuilder.append("Error Details:\n").append(e.getMessage()).append("\n");
        }

        // 한 번에 로그 출력
        logger.info(logBuilder.toString());
    }
}
