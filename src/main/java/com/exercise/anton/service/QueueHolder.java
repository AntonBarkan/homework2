package com.exercise.anton.service;

import com.exercise.anton.model.Row;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class QueueHolder {

    private final Queue<Row> queue = new ConcurrentLinkedQueue<>();


    public void add(Row payload) {
        queue.add(payload);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Row poll() {
        return queue.poll();
    }
}

