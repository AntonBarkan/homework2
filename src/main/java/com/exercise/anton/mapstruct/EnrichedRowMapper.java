package com.exercise.anton.mapstruct;

import com.exercise.anton.model.Row;
import com.exercise.anton.model.rest.EnrichedRow;
import com.exercise.anton.model.rest.TestApiResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EnrichedRowMapper {

    EnrichedRow map(TestApiResponseDto testApiResponseDto, Row row);
}
