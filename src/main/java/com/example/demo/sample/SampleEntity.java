package com.example.demo.sample;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
public class SampleEntity {

    private Long id; // Partition Key
    private Long createdAt; // Sort Key
    private String name;

    @DynamoDbPartitionKey
    public Long getId() {
        return id;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("created_at") // 테이블의 정렬 키 이름에 맞게 매핑
    public Long getCreatedAt() {
        return createdAt;
    }

    @DynamoDbAttribute("name")
    public String getName() {
        return name;
    }
}
