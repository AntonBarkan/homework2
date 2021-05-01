package com.exercise.anton.service;

import com.exercise.anton.config.IntegrationConfiguration;
import com.exercise.anton.model.Row;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class CsvReaderService {

    private final Logger logger = LoggerFactory.getLogger(CsvReaderService.class);

    private final IntegrationConfiguration.Gateway gateway;

    private final BatchesHolder batchesHolder;

    private final CsvMapper csvMapper;

    private final CsvSchema orderLineSchema;

    public CsvReaderService(
            IntegrationConfiguration.Gateway gateway,
            BatchesHolder batchesHolder
    ) {
        this.gateway = gateway;
        this.batchesHolder = batchesHolder;
        orderLineSchema = CsvSchema.emptySchema().withHeader();
        csvMapper = new CsvMapper();
    }

    public void readFromFile(File file) throws IOException {
        csvMapper.readerFor(Row.class)
                .with(orderLineSchema)
                .readValues(file)
                .forEachRemaining(row -> {
                            batchesHolder.addBatchItem(((Row)row).getBatch());
                            gateway.sendMassage((Row)row);
                        }
                );
        batchesHolder.endOfFile();
        logger.info("File {} read.", file.getAbsolutePath());
    }
}
