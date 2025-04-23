package com.catalis.core.modelhub.models.repositories;

import com.catalis.core.modelhub.models.entities.VirtualEntityField;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for VirtualEntityField operations.
 */
@Repository
public interface VirtualEntityFieldRepository extends ReactiveCrudRepository<VirtualEntityField, UUID> {

    /**
     * Find all fields for a virtual entity.
     *
     * @param entityId the ID of the entity
     * @return a Flux emitting the found fields
     */
    Flux<VirtualEntityField> findByEntityId(UUID entityId);

    /**
     * Find all fields for a virtual entity, ordered by order_index.
     *
     * @param entityId the ID of the entity
     * @return a Flux emitting the found fields
     */
    @Query("SELECT * FROM virtual_entity_field WHERE entity_id = :entityId ORDER BY order_index ASC")
    Flux<VirtualEntityField> findByEntityIdOrderByOrderIndex(UUID entityId);

    /**
     * Find a field by entity ID and field key.
     *
     * @param entityId the ID of the entity
     * @param fieldKey the key of the field
     * @return a Mono emitting the found field or empty if not found
     */
    Mono<VirtualEntityField> findByEntityIdAndFieldKey(UUID entityId, String fieldKey);

    /**
     * Delete all fields for a virtual entity.
     *
     * @param entityId the ID of the entity
     * @return a Mono emitting the number of deleted fields
     */
    Mono<Void> deleteByEntityId(UUID entityId);
}