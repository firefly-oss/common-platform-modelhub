package com.firefly.core.modelhub.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;
import io.r2dbc.postgresql.codec.Json;

/**
 * Entity representing a record for a virtual entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("virtual_entity_record")
public class VirtualEntityRecord {

    @Id
    private UUID id;

    @Column("entity_id")
    private UUID entityId;

    @Column("payload")
    private Json payload;

    @CreatedBy
    @Column("created_by")
    private String createdBy;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column("updated_by")
    private String updatedBy;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}