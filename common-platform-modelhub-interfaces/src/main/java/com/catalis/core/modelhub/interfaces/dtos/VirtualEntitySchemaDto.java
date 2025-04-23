package com.catalis.core.modelhub.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

/**
 * DTO for virtual entity schema operations.
 * This represents the complete schema of a virtual entity, including all its fields.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Virtual entity schema definition")
public class VirtualEntitySchemaDto {

    @Schema(description = "Entity information")
    private VirtualEntityDto entity;

    @Schema(description = "Entity fields")
    private List<VirtualEntityFieldDto> fields;
}