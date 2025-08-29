package com.firefly.core.modelhub.models.repositories;

import com.firefly.core.modelhub.interfaces.dtos.QueryDto;
import com.firefly.core.modelhub.models.entities.VirtualEntityRecord;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Custom repository interface for dynamic queries on VirtualEntityRecord.
 */
public interface VirtualEntityRecordRepositoryCustom {

    /**
     * Execute a dynamic query based on the provided query DTO.
     *
     * @param queryDto the query definition
     * @return a Flux emitting the matching records
     */
    Flux<VirtualEntityRecord> executeQuery(QueryDto queryDto);

    /**
     * Count the number of records matching a query.
     *
     * @param queryDto the query definition
     * @return a Mono emitting the count
     */
    Mono<Long> countQuery(QueryDto queryDto);
}