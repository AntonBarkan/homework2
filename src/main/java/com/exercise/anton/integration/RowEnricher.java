package com.exercise.anton.integration;

import com.exercise.anton.mapstruct.EnrichedRowMapper;
import com.exercise.anton.model.Row;
import com.exercise.anton.rest.client.RestClient;
import com.exercise.anton.service.BatchesHolder;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@AllArgsConstructor
public class RowEnricher implements MessageHandler {

    private final RestClient restClient;

    private final EnrichedRowMapper enrichedRowMapper;

    private final BatchesHolder batchesHolder;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        Row row = (Row) message.getPayload();
        restClient.testApi(row)
                .map(response ->
                        enrichedRowMapper.map(response, row))
                .flatMap(restClient::item)
                .flatMap(m -> batchesHolder.removeBatchItem(row.getBatch()))
                .subscribe();

    }
}
