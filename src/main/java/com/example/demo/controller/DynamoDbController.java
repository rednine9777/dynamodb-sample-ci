package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import com.example.demo.sample.SampleEntity;
import com.example.demo.sample.SampleRepository;

import java.util.List;

@RestController
public class DynamoDbController {

    private final DynamoDbClient dynamoDbClient;
    private final SampleRepository sampleRepository;

    public DynamoDbController(DynamoDbClient dynamoDbClient, SampleRepository sampleRepository) {
        this.dynamoDbClient = dynamoDbClient;
        this.sampleRepository = sampleRepository;
    }

    @GetMapping("/")
    public String getDynamoDbInfo() {
        StringBuilder html = new StringBuilder();

        try {
            // DynamoDB 연결 확인
            html.append("<html>")
                .append("<head><title>DynamoDB Information</title></head>")
                .append("<body>")
                .append("<h1 style='color:green;'>DynamoDB Connection: SUCCESS</h1>");

            // DynamoDB 테이블 목록 출력
            html.append("<h2>Table: DYNAMO_DB_SAMPLE</h2>")
                .append("<table border='1'>")
                .append("<tr><th>ID</th><th>Created At</th><th>Name</th></tr>");

            // 현재 데이터 로드
            List<SampleEntity> items = sampleRepository.findAll();
            for (SampleEntity item : items) {
                html.append("<tr>")
                    .append("<td>").append(item.getId()).append("</td>")
                    .append("<td>").append(item.getCreatedAt()).append("</td>")
                    .append("<td>").append(item.getName()).append("</td>")
                    .append("</tr>");
            }
            html.append("</table>");

            // CRUD 입력 폼 및 버튼
            html.append("<h2>CRUD Operations</h2>")
                .append("<form action='/api/sample' method='post'>")
                .append("ID: <input type='number' name='id'><br>")
                .append("Created At: <input type='number' name='createdAt'><br>")
                .append("Name: <input type='text' name='name'><br>")
                .append("<button type='submit'>Create</button>")
                .append("</form>")
                .append("<br>")
                .append("<form action='/api/sample' method='get'>")
                .append("ID: <input type='number' name='id'><br>")
                .append("Created At: <input type='number' name='createdAt'><br>")
                .append("<button type='submit'>Retrieve</button>")
                .append("</form>")
                .append("<br>")
                .append("<form action='/api/sample' method='delete'>")
                .append("ID: <input type='number' name='id'><br>")
                .append("Created At: <input type='number' name='createdAt'><br>")
                .append("<button type='submit'>Delete</button>")
                .append("</form>")
                .append("</body>")
                .append("</html>");
        } catch (Exception e) {
            // DynamoDB 연결 실패 시
            html.append("<html>")
                .append("<head><title>DynamoDB Information</title></head>")
                .append("<body>")
                .append("<h1 style='color:red;'>DynamoDB Connection: FAILED</h1>")
                .append("<p>Error: ").append(e.getMessage()).append("</p>")
                .append("</body>")
                .append("</html>");
        }

        return html.toString();
    }

    // CRUD 처리
    @PostMapping("/api/sample")
    public void createSample(@RequestParam Long id, @RequestParam Long createdAt, @RequestParam String name) {
        SampleEntity entity = new SampleEntity();
        entity.setId(id);
        entity.setCreatedAt(createdAt);
        entity.setName(name);
        sampleRepository.save(entity);

        logCurrentTableState("CREATE", id, createdAt, name);
    }

    @GetMapping("/api/sample")
    public SampleEntity getSample(@RequestParam Long id, @RequestParam Long createdAt) {
        SampleEntity entity = sampleRepository.getById(id, createdAt);
        logCurrentTableState("RETRIEVE", id, createdAt, entity != null ? entity.getName() : "NOT FOUND");
        return entity;
    }

    @DeleteMapping("/api/sample")
    public void deleteSample(@RequestParam Long id, @RequestParam Long createdAt) {
        sampleRepository.delete(id, createdAt);
        logCurrentTableState("DELETE", id, createdAt, "DELETED");
    }

    private void logCurrentTableState(String operation, Long id, Long createdAt, String detail) {
        System.out.println("\n==============================================");
        System.out.println("OPERATION: " + operation);
        System.out.printf("ID: %d, Created At: %d, Detail: %s%n", id, createdAt, detail);
        System.out.println("Current items in DYNAMO_DB_SAMPLE:");
        System.out.println("-----------------------------------------------");
        System.out.printf("| %-10s | %-20s | %-20s |%n", "ID", "Created At", "Name");
        System.out.println("-----------------------------------------------");

        List<SampleEntity> items = sampleRepository.findAll();
        for (SampleEntity item : items) {
            System.out.printf("| %-10d | %-20d | %-20s |%n", item.getId(), item.getCreatedAt(), item.getName());
        }
        System.out.println("-----------------------------------------------\n");
    }
}