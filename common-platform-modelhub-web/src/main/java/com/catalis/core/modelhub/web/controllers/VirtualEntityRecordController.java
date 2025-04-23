package com.catalis.core.modelhub.web.controllers;

import com.catalis.core.modelhub.core.services.VirtualEntityRecordService;
import com.catalis.core.modelhub.interfaces.dtos.QueryDto;
import com.catalis.core.modelhub.interfaces.dtos.VirtualEntityRecordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * REST controller for virtual entity record operations.
 */
@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Virtual Entity Records", description = "API for managing virtual entity records")
public class VirtualEntityRecordController {

    private final VirtualEntityRecordService recordService;

    @GetMapping(value = "/entity/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get all records for an entity",
            description = "Returns a list of all records for a specific virtual entity",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Entity not found", content = @Content)
            }
    )
    public ResponseEntity<Flux<VirtualEntityRecordDto>> getRecordsByEntityId(
            @Parameter(description = "ID of the entity to retrieve records for", required = true)
            @PathVariable UUID entityId,
            @Parameter(description = "Page number (zero-based)")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(required = false, defaultValue = "20") int size) {
        return ResponseEntity.ok(recordService.getRecordsByEntityId(entityId, PageRequest.of(page, size)));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get a record by ID",
            description = "Returns a virtual entity record by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Record not found", content = @Content)
            }
    )
    public Mono<ResponseEntity<VirtualEntityRecordDto>> getRecordById(
            @Parameter(description = "ID of the record to retrieve", required = true)
            @PathVariable UUID id) {
        return recordService.getRecordById(id)
                .switchIfEmpty(Mono.error(new VirtualEntityController.ResourceNotFoundException("Record not found with ID: " + id)))
                .map(ResponseEntity::ok);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Create a new record",
            description = "Creates a new record for a virtual entity",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Record created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input or validation error", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Entity not found", content = @Content)
            }
    )
    public Mono<ResponseEntity<VirtualEntityRecordDto>> createRecord(
            @Parameter(description = "Record to create", required = true, schema = @Schema(implementation = VirtualEntityRecordDto.class))
            @Valid @RequestBody VirtualEntityRecordDto recordDto) {
        return recordService.createRecord(recordDto)
                .map(record -> ResponseEntity.status(HttpStatus.CREATED).body(record));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Update a record",
            description = "Updates an existing record",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Record updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input or validation error", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Record not found", content = @Content)
            }
    )
    public Mono<ResponseEntity<VirtualEntityRecordDto>> updateRecord(
            @Parameter(description = "ID of the record to update", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Updated record data", required = true, schema = @Schema(implementation = VirtualEntityRecordDto.class))
            @Valid @RequestBody VirtualEntityRecordDto recordDto) {
        return recordService.updateRecord(id, recordDto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a record",
            description = "Deletes a record",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Record deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Record not found", content = @Content)
            }
    )
    public Mono<ResponseEntity<Void>> deleteRecord(
            @Parameter(description = "ID of the record to delete", required = true)
            @PathVariable UUID id) {
        return recordService.deleteRecord(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @PostMapping(
            value = "/query",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Query records",
            description = "Executes a query against virtual entity records",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Query executed successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid query", content = @Content)
            }
    )
    public Mono<ResponseEntity<Map<String, Object>>> queryRecords(
            @Parameter(description = "Query definition", required = true, schema = @Schema(implementation = QueryDto.class))
            @Valid @RequestBody QueryDto queryDto) {

        return recordService.countQuery(queryDto)
                .flatMap(count -> {
                    Flux<VirtualEntityRecordDto> records = recordService.executeQuery(queryDto);

                    return records.collectList()
                            .map(recordList -> {
                                Map<String, Object> response = new HashMap<>();
                                response.put("totalCount", count);
                                response.put("page", queryDto.getPage());
                                response.put("size", queryDto.getSize());
                                response.put("records", recordList);

                                return ResponseEntity.ok(response);
                            });
                });
    }
}
