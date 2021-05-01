package com.exercise.anton.integration;

import com.exercise.anton.model.Row;
import com.exercise.anton.service.QueueHolder;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@AllArgsConstructor
public class QueueWriter implements MessageHandler {

    private final QueueHolder queueHolder;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        queueHolder.add((Row) message.getPayload());
    }
}
