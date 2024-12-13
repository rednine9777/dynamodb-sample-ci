// package com.example.demo.controller;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;
// import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

// import java.util.List;

// @RestController
// public class DynamoDbController {

//     private final DynamoDbClient dynamoDbClient;

//     @Value("${aws.region}") // 애플리케이션 설정에서 리전 정보 읽기
//     private String region;

//     public DynamoDbController(DynamoDbClient dynamoDbClient) {
//         this.dynamoDbClient = dynamoDbClient;
//     }

//     @GetMapping("/")
//     public String getDynamoDbInfo() {
//         StringBuilder html = new StringBuilder();

//         try {
//             // DynamoDB 연결 확인
//             html.append("<html>")
//                 .append("<head><title>DynamoDB Information</title></head>")
//                 .append("<body>")
//                 .append("<h1 style='color:green;'>DynamoDB Connection: SUCCESS</h1>");

//             // 리전 정보 출력
//             html.append("<h2>AWS Information:</h2>")
//                 .append("<table border='1'>")
//                 .append("<tr><th>Property</th><th>Value</th></tr>")
//                 .append("<tr><td>Region</td><td>").append(region).append("</td></tr>")
//                 .append("</table>");

//             // DynamoDB 테이블 목록 출력
//             List<String> tableNames = dynamoDbClient.listTables().tableNames();

//             html.append("<h2>DynamoDB Tables:</h2>")
//                 .append("<table border='1'>")
//                 .append("<tr><th>Table Name</th></tr>");

//             for (String tableName : tableNames) {
//                 html.append("<tr><td>").append(tableName).append("</td></tr>");
//             }

//             html.append("</table>")
//                 .append("</body>")
//                 .append("</html>");
//         } catch (Exception e) {
//             // DynamoDB 연결 실패 시
//             html.append("<html>")
//                 .append("<head><title>DynamoDB Information</title></head>")
//                 .append("<body>")
//                 .append("<h1 style='color:red;'>DynamoDB Connection: FAILED</h1>")
//                 .append("<p>Error: ").append(e.getMessage()).append("</p>")
//                 .append("</body>")
//                 .append("</html>");
//         }

//         return html.toString();
//     }
// }


////////////////////////////////////////// V2 //////////////////////////
// package com.example.demo.controller;

// import org.springframework.web.bind.annotation.*;
// import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
// import com.example.demo.sample.SampleEntity;
// import com.example.demo.sample.SampleRepository;

// import java.util.List;

// @RestController
// public class DynamoDbController {

//     private final DynamoDbClient dynamoDbClient;
//     private final SampleRepository sampleRepository;

//     public DynamoDbController(DynamoDbClient dynamoDbClient, SampleRepository sampleRepository) {
//         this.dynamoDbClient = dynamoDbClient;
//         this.sampleRepository = sampleRepository;
//     }

//     @GetMapping("/")
//     public String getDynamoDbInfo() {
//         StringBuilder html = new StringBuilder();

//         try {
//             // DynamoDB 연결 확인
//             html.append("<html>")
//                 .append("<head><title>DynamoDB Information</title></head>")
//                 .append("<body>")
//                 .append("<h1 style='color:green;'>DynamoDB Connection: SUCCESS</h1>");

//             // DynamoDB 테이블 목록 출력
//             html.append("<h2>Table: DYNAMO_DB_SAMPLE</h2>")
//                 .append("<table border='1'>")
//                 .append("<tr><th>ID</th><th>Created At</th><th>Name</th></tr>");

//             // 현재 데이터 로드
//             List<SampleEntity> items = sampleRepository.findAll();
//             for (SampleEntity item : items) {
//                 html.append("<tr>")
//                     .append("<td>").append(item.getId()).append("</td>")
//                     .append("<td>").append(item.getCreatedAt()).append("</td>")
//                     .append("<td>").append(item.getName()).append("</td>")
//                     .append("</tr>");
//             }
//             html.append("</table>");

//             // CRUD 입력 폼 및 버튼
//             html.append("<h2>CRUD Operations</h2>")
//                 .append("<form action='/api/sample' method='post'>")
//                 .append("ID: <input type='number' name='id'><br>")
//                 .append("Created At: <input type='number' name='createdAt'><br>")
//                 .append("Name: <input type='text' name='name'><br>")
//                 .append("<button type='submit'>Create</button>")
//                 .append("</form>")
//                 .append("<br>")
//                 .append("<form action='/api/sample' method='get'>")
//                 .append("ID: <input type='number' name='id'><br>")
//                 .append("Created At: <input type='number' name='createdAt'><br>")
//                 .append("<button type='submit'>Retrieve</button>")
//                 .append("</form>")
//                 .append("<br>")
//                 .append("<form action='/api/sample' method='delete'>")
//                 .append("ID: <input type='number' name='id'><br>")
//                 .append("Created At: <input type='number' name='createdAt'><br>")
//                 .append("<button type='submit'>Delete</button>")
//                 .append("</form>")
//                 .append("</body>")
//                 .append("</html>");
//         } catch (Exception e) {
//             // DynamoDB 연결 실패 시
//             html.append("<html>")
//                 .append("<head><title>DynamoDB Information</title></head>")
//                 .append("<body>")
//                 .append("<h1 style='color:red;'>DynamoDB Connection: FAILED</h1>")
//                 .append("<p>Error: ").append(e.getMessage()).append("</p>")
//                 .append("</body>")
//                 .append("</html>");
//         }

//         return html.toString();
//     }

//     // CRUD 처리
//     @PostMapping("/api/sample")
//     public void createSample(@RequestParam Long id, @RequestParam Long createdAt, @RequestParam String name) {
//         SampleEntity entity = new SampleEntity();
//         entity.setId(id);
//         entity.setCreatedAt(createdAt);
//         entity.setName(name);
//         sampleRepository.save(entity);
//     }

//     @GetMapping("/api/sample")
//     public SampleEntity getSample(@RequestParam Long id, @RequestParam Long createdAt) {
//         return sampleRepository.getById(id, createdAt);
//     }

//     @DeleteMapping("/api/sample")
//     public void deleteSample(@RequestParam Long id, @RequestParam Long createdAt) {
//         sampleRepository.delete(id, createdAt);
//     }
// }
