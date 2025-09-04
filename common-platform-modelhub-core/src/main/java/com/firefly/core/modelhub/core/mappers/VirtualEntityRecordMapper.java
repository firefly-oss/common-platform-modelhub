/*
 * Copyright 2025 Firefly Software Solutions Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.core.modelhub.core.mappers;

import com.firefly.core.modelhub.interfaces.dtos.VirtualEntityRecordDto;
import com.firefly.core.modelhub.models.entities.VirtualEntityRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Map;

/**
 * Mapper for converting between VirtualEntityRecord entity and VirtualEntityRecordDto.
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VirtualEntityRecordMapper {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Maps a VirtualEntityRecord entity to a VirtualEntityRecordDto.
     *
     * @param entity the entity to map
     * @return the mapped DTO
     */
    @Mapping(source = "payload", target = "payload", qualifiedByName = "jsonToMap")
    VirtualEntityRecordDto toDto(VirtualEntityRecord entity);

    /**
     * Maps a VirtualEntityRecordDto to a VirtualEntityRecord entity.
     *
     * @param dto the DTO to map
     * @return the mapped entity
     */
    @Mapping(source = "payload", target = "payload", qualifiedByName = "mapToJson")
    VirtualEntityRecord toEntity(VirtualEntityRecordDto dto);

    /**
     * Converts a Json object to a Map.
     *
     * @param json the Json object to convert
     * @return the converted Map
     */
    @Named("jsonToMap")
    @SuppressWarnings("unchecked")
    default Map<String, Object> jsonToMap(Json json) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json.asString(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to Map", e);
        }
    }

    /**
     * Converts a Map to a Json object.
     *
     * @param map the Map to convert
     * @return the converted Json object
     */
    @Named("mapToJson")
    default Json mapToJson(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        try {
            String jsonString = OBJECT_MAPPER.writeValueAsString(map);
            return Json.of(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Map to JSON", e);
        }
    }
}