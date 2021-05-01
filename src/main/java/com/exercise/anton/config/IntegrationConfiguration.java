package com.exercise.anton.config;

import com.exercise.anton.integration.QueueReader;
import com.exercise.anton.integration.QueueWriter;
import com.exercise.anton.integration.RowEnricher;
import com.exercise.anton.mapstruct.EnrichedRowMapper;
import com.exercise.anton.model.Row;
import com.exercise.anton.rest.client.RestClient;
import com.exercise.anton.service.BatchesHolder;
import com.exercise.anton.service.QueueHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableIntegration
public class IntegrationConfiguration {

    @Bean
    @ServiceActivator(inputChannel = "rowChannel")
    public MessageHandler queueWriter(QueueHolder queueHolder) {
        return new QueueWriter(queueHolder);
    }

    @Bean
    @ServiceActivator(inputChannel = "messageChannel")
    public MessageHandler messageChannelMessageHandler(RestClient restClient, EnrichedRowMapper enrichedRowMapper, BatchesHolder batchesHolder) {
        return new RowEnricher(restClient, enrichedRowMapper, batchesHolder);
    }

    @Bean
    @InboundChannelAdapter(value = "messageChannel",
            poller = @Poller(fixedDelay = "100", maxMessagesPerPoll = "20")
    )
    public MessageSource<Row> queueReader(QueueHolder queueHolder) {
        return new QueueReader(queueHolder);
    }

    @MessagingGateway(defaultRequestChannel = "rowChannel")
    public interface Gateway {
        void sendMassage(Row data);
    }

    @Bean
    public MessageChannel messageChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel rowChannel() {
        return new DirectChannel();
    }
}
