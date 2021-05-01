package com.exercise.anton.rest.client;

import com.exercise.anton.model.Row;
import com.exercise.anton.model.rest.BatchId;
import com.exercise.anton.model.rest.EnrichedRow;
import com.exercise.anton.model.rest.TestApiResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class RestClient {

    private final WebClient webClient;

    public Mono<ResponseEntity<Void>> batchComplete(String batchId) {
        return webClient.post().uri("batch")
                .bodyValue(new BatchId(batchId))
                .retrieve()
                .toBodilessEntity();
    }

    public Mono<TestApiResponseDto> testApi(Row row ) {
        return webClient.post().uri("test_api")
                .bodyValue(row)
                .retrieve()
                .bodyToMono(TestApiResponseDto.class);
    }

    public Mono<ResponseEntity<Void>> item(EnrichedRow enrichedRow) {
        return webClient.post().uri("item")
                .bodyValue(enrichedRow)
                .retrieve()
                .toBodilessEntity();
    }
}
