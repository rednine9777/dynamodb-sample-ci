package com.example.demo.sample;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SampleRepository {

    private final DynamoDbTable<SampleEntity> sampleTable;

    public SampleRepository(DynamoDbEnhancedClient enhancedClient) {
        this.sampleTable = enhancedClient.table("DYNAMO_DB_SAMPLE", TableSchema.fromBean(SampleEntity.class));
    }

    public void save(SampleEntity entity) {
        sampleTable.putItem(entity);
    }

    public SampleEntity getById(Long id, Long createdAt) {
        return sampleTable.getItem(r -> r.key(k -> k.partitionValue(id).sortValue(createdAt)));
    }

    public void delete(Long id, Long createdAt) {
        sampleTable.deleteItem(r -> r.key(k -> k.partitionValue(id).sortValue(createdAt)));
    }

    public List<SampleEntity> findAll() {
        List<SampleEntity> items = new ArrayList<>();
        sampleTable.scan().items().forEach(items::add);
        return items;
    }
}
