package com.catalis.core.modelhub.core.mappers;

import com.catalis.core.modelhub.interfaces.dtos.VirtualEntityFieldDto;
import com.catalis.core.modelhub.models.entities.VirtualEntityField;
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
 * Mapper for converting between VirtualEntityField entity and VirtualEntityFieldDto.
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VirtualEntityFieldMapper {

    /**
     * Maps a VirtualEntityField entity to a VirtualEntityFieldDto.
     *
     * @param entity the entity to map
     * @return the mapped DTO
     */
    @Mapping(source = "options", target = "options", qualifiedByName = "jsonToMap")
    VirtualEntityFieldDto toDto(VirtualEntityField entity);

    /**
     * Maps a VirtualEntityFieldDto to a VirtualEntityField entity.
     *
     * @param dto the DTO to map
     * @return the mapped entity
     */
    @Mapping(source = "options", target = "options", qualifiedByName = "mapToJson")
    VirtualEntityField toEntity(VirtualEntityFieldDto dto);

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
