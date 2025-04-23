package com.catalis.core.modelhub.models.repositories;

import com.catalis.core.modelhub.models.entities.VirtualEntityRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for VirtualEntityRecord operations.
 */
@Repository
public interface VirtualEntityRecordRepository extends R2dbcRepository<VirtualEntityRecord, UUID>, VirtualEntityRecordRepositoryCustom {

    /**
     * Find all records for a virtual entity.
     *
     * @param entityId the ID of the entity
     * @return a Flux emitting the found records
     */
    Flux<VirtualEntityRecord> findByEntityId(UUID entityId);

    /**
     * Find all records for a virtual entity with pagination.
     *
     * @param entityId the ID of the entity
     * @param pageable the pagination information
     * @return a Flux emitting the found records
     */
    Flux<VirtualEntityRecord> findByEntityId(UUID entityId, Pageable pageable);

    /**
     * Count the number of records for a virtual entity.
     *
     * @param entityId the ID of the entity
     * @return a Mono emitting the count
     */
    Mono<Long> countByEntityId(UUID entityId);

    /**
     * Find records by entity ID and a JSON field value.
     *
     * @param entityId the ID of the entity
     * @param fieldKey the key of the field
     * @param value the value to search for
     * @return a Flux emitting the found records
     */
    @Query("SELECT * FROM virtual_entity_record WHERE entity_id = :entityId AND payload->>:fieldKey = :value")
    Flux<VirtualEntityRecord> findByEntityIdAndFieldValue(UUID entityId, String fieldKey, String value);

    /**
     * Delete all records for a virtual entity.
     *
     * @param entityId the ID of the entity
     * @return a Mono emitting the number of deleted records
     */
    Mono<Void> deleteByEntityId(UUID entityId);
}
