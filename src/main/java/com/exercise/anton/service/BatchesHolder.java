package com.exercise.anton.service;

import com.exercise.anton.rest.client.RestClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class BatchesHolder {

    private final RestClient restClient;

    private final Map<String, Integer> map = new ConcurrentHashMap<>();

    private final Queue<String> queue = new ConcurrentLinkedQueue<>();

    private final Lock fileEndLocker = new ReentrantLock();

    private volatile boolean isFileEnded = false;

    public BatchesHolder(RestClient restClient) {
        this.restClient = restClient;
    }

    public void endOfFile() {
        fileEndLocker.lock();
        try {
            isFileEnded = true;
        } finally {
            fileEndLocker.unlock();
        }
        queue.forEach(
                batchId -> {
                    restClient.batchComplete(batchId).block();
                    map.remove(batchId);
                });

    }

    public void addBatchItem(String batchId) {
        map.putIfAbsent(batchId, 0);
        map.computeIfPresent(batchId, (key, val) -> ++val);
    }

    public Mono<?> removeBatchItem(String batchId) {
        var counter = map.computeIfPresent(batchId, (key, val) -> --val);
        if (Objects.isNull(counter) || counter == 0) {
            if (isFileEnded) {
                map.remove(batchId);
                return restClient.batchComplete(batchId);
            } else {
                fileEndLocker.lock();
                try {
                    if (isFileEnded) {
                        return restClient.batchComplete(batchId);
                    } else {
                        queue.add(batchId);
                    }
                } finally {
                    fileEndLocker.unlock();
                }
            }
        }
        return Mono.empty();
    }
}
