package com.firefly.core.modelhub.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for virtual entity field operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Virtual entity field definition")
public class VirtualEntityFieldDto {

    @Schema(description = "Unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotNull(message = "Entity ID is required")
    @Schema(description = "Parent entity ID", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID entityId;

    @NotBlank(message = "Field key is required")
    @Schema(description = "Field key (used in code/API)", example = "firstName", required = true)
    private String fieldKey;

    @Schema(description = "Field label (displayed to users)", example = "First Name")
    private String fieldLabel;

    @NotBlank(message = "Field type is required")
    @Schema(description = "Field data type", example = "string", required = true)
    private String fieldType;

    @Schema(description = "Whether the field is required", example = "true")
    private Boolean required;

    @Schema(description = "Additional field options (validation, UI hints, etc.)")
    private Map<String, Object> options;

    @Schema(description = "Display order index", example = "1")
    private Integer orderIndex;

    @Schema(description = "Created by user", example = "admin")
    private String createdBy;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last updated by user", example = "admin")
    private String updatedBy;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}