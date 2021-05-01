package com.exercise.anton.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@AllArgsConstructor
public class WebClientConfiguration {

    private final AppProperties appProperties;

    @Bean
    WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000)
                .responseTimeout(Duration.ofMinutes(1))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(1, TimeUnit.MINUTES))
                                .addHandlerLast(new WriteTimeoutHandler(1, TimeUnit.MINUTES)));
        ExchangeStrategies strategies = ExchangeStrategies
                .builder()
                .codecs(clientDefaultCodecsConfigurer -> {
                    clientDefaultCodecsConfigurer.defaultCodecs()
                            .jackson2JsonEncoder(new Jackson2JsonEncoder(
                                    new ObjectMapper(),
                                    MediaType.APPLICATION_JSON)
                            );
                    clientDefaultCodecsConfigurer.defaultCodecs()
                            .jackson2JsonEncoder(new Jackson2JsonEncoder(
                                    new ObjectMapper(),
                                    MediaType.APPLICATION_OCTET_STREAM)
                            );
                    clientDefaultCodecsConfigurer.defaultCodecs()
                            .jackson2JsonDecoder(new Jackson2JsonDecoder(
                                    new ObjectMapper(),
                                    MediaType.APPLICATION_JSON)
                            );
                    clientDefaultCodecsConfigurer.defaultCodecs()
                            .jackson2JsonDecoder(new Jackson2JsonDecoder(
                                    new ObjectMapper(),
                                    MediaType.APPLICATION_OCTET_STREAM)
                            );
                }).build();

        return WebClient.builder()
                .baseUrl(appProperties.getServer())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies)
                .build();
    }
}
