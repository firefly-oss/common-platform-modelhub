package com.catalis.core.modelhub.models.repositories;

import com.catalis.core.modelhub.models.entities.VirtualEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository for VirtualEntity operations.
 */
@Repository
public interface VirtualEntityRepository extends ReactiveCrudRepository<VirtualEntity, UUID> {

    /**
     * Find a virtual entity by name.
     *
     * @param name the name of the entity
     * @return a Mono emitting the found entity or empty if not found
     */
    Mono<VirtualEntity> findByName(String name);

    /**
     * Find a virtual entity by name and active status.
     *
     * @param name   the name of the entity
     * @param active the active status
     * @return a Mono emitting the found entity or empty if not found
     */
    Mono<VirtualEntity> findByNameAndActive(String name, Boolean active);

    /**
     * Check if a virtual entity exists by name.
     *
     * @param name the name of the entity
     * @return a Mono emitting true if the entity exists, false otherwise
     */
    @Query("SELECT EXISTS(SELECT 1 FROM virtual_entity WHERE name = :name)")
    Mono<Boolean> existsByName(String name);
}