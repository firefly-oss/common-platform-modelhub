package com.catalis.core.modelhub.core.mappers;

import com.catalis.core.modelhub.interfaces.dtos.VirtualEntityDto;
import com.catalis.core.modelhub.models.entities.VirtualEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for converting between VirtualEntity entity and VirtualEntityDto.
 */
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VirtualEntityMapper {

    /**
     * Maps a VirtualEntity entity to a VirtualEntityDto.
     *
     * @param entity the entity to map
     * @return the mapped DTO
     */
    VirtualEntityDto toDto(VirtualEntity entity);

    /**
     * Maps a VirtualEntityDto to a VirtualEntity entity.
     *
     * @param dto the DTO to map
     * @return the mapped entity
     */
    VirtualEntity toEntity(VirtualEntityDto dto);
}