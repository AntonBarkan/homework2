package com.exercise.anton;

import com.exercise.anton.config.AppProperties;
import com.exercise.anton.service.CsvReaderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class AntonApplication implements CommandLineRunner {

	private final CsvReaderService csvReaderService;

	public AntonApplication(CsvReaderService csvReaderService) {
		this.csvReaderService = csvReaderService;
	}

	public static void main(String[] args) {
		SpringApplication.run(AntonApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {
		csvReaderService.readFromFile(ResourceUtils.getFile("classpath:source_data.csv"));
	}
}
