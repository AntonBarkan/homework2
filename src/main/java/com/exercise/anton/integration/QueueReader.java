package com.exercise.anton.integration;

import com.exercise.anton.model.Row;
import com.exercise.anton.service.QueueHolder;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

public class QueueReader implements MessageSource<Row> {

    private final QueueHolder queueHolder;

    public QueueReader(QueueHolder queueHolder) {
        this.queueHolder = queueHolder;
    }

    @Override
    public Message<Row> receive() {
        if (queueHolder.isEmpty()) {
            return null;
        }
        return MessageBuilder.withPayload(queueHolder.poll()).build();
    }
}
