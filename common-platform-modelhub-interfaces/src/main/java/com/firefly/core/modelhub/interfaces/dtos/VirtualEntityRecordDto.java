package com.firefly.core.modelhub.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for virtual entity record operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Virtual entity record")
public class VirtualEntityRecordDto {

    @Schema(description = "Unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotNull(message = "Entity ID is required")
    @Schema(description = "Parent entity ID", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID entityId;

    @NotNull(message = "Payload is required")
    @Schema(description = "Record data payload", required = true)
    private Map<String, Object> payload;

    @Schema(description = "Created by user", example = "admin")
    private String createdBy;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last updated by user", example = "admin")
    private String updatedBy;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}