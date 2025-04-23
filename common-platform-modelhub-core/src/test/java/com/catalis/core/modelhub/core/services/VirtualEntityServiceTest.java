package com.catalis.core.modelhub.core.services;

import com.catalis.core.modelhub.core.mappers.VirtualEntityMapper;
import com.catalis.core.modelhub.interfaces.dtos.VirtualEntityDto;
import com.catalis.core.modelhub.interfaces.dtos.VirtualEntityFieldDto;
import com.catalis.core.modelhub.interfaces.dtos.VirtualEntitySchemaDto;
import com.catalis.core.modelhub.models.entities.VirtualEntity;
import com.catalis.core.modelhub.models.repositories.VirtualEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VirtualEntityServiceTest {

    @Mock
    private VirtualEntityRepository virtualEntityRepository;

    @Mock
    private VirtualEntityFieldService virtualEntityFieldService;

    @Mock
    private VirtualEntityRecordService virtualEntityRecordService;

    @Mock
    private VirtualEntityMapper virtualEntityMapper;

    @InjectMocks
    private VirtualEntityService virtualEntityService;

    private UUID entityId;
    private VirtualEntity entity;
    private VirtualEntityDto entityDto;
    private VirtualEntityFieldDto fieldDto;

    @BeforeEach
    void setUp() {
        entityId = UUID.randomUUID();

        entity = VirtualEntity.builder()
                .id(entityId)
                .name("TestEntity")
                .description("Test Entity Description")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        entityDto = VirtualEntityDto.builder()
                .id(entityId)
                .name("TestEntity")
                .description("Test Entity Description")
                .active(true)
                .build();

        fieldDto = VirtualEntityFieldDto.builder()
                .id(UUID.randomUUID())
                .entityId(entityId)
                .fieldKey("testField")
                .fieldLabel("Test Field")
                .fieldType("string")
                .required(true)
                .orderIndex(1)
                .build();
    }

    @Test
    void getAllEntities_ShouldReturnEntities() {
        when(virtualEntityRepository.findAll()).thenReturn(Flux.just(entity));
        when(virtualEntityMapper.toDto(entity)).thenReturn(entityDto);

        StepVerifier.create(virtualEntityService.getAllEntities())
                .expectNext(entityDto)
                .verifyComplete();

        verify(virtualEntityRepository).findAll();
        verify(virtualEntityMapper).toDto(entity);
    }

    @Test
    void getEntityById_ShouldReturnEntity() {
        when(virtualEntityRepository.findById(entityId)).thenReturn(Mono.just(entity));
        when(virtualEntityMapper.toDto(entity)).thenReturn(entityDto);

        StepVerifier.create(virtualEntityService.getEntityById(entityId))
                .expectNext(entityDto)
                .verifyComplete();

        verify(virtualEntityRepository).findById(entityId);
        verify(virtualEntityMapper).toDto(entity);
    }

    @Test
    void getEntityById_WhenNotFound_ShouldReturnEmpty() {
        when(virtualEntityRepository.findById(entityId)).thenReturn(Mono.empty());

        StepVerifier.create(virtualEntityService.getEntityById(entityId))
                .verifyComplete();

        verify(virtualEntityRepository).findById(entityId);
        verify(virtualEntityMapper, never()).toDto(any());
    }

    @Test
    void getEntityByName_ShouldReturnEntity() {
        String entityName = "TestEntity";
        when(virtualEntityRepository.findByName(entityName)).thenReturn(Mono.just(entity));
        when(virtualEntityMapper.toDto(entity)).thenReturn(entityDto);

        StepVerifier.create(virtualEntityService.getEntityByName(entityName))
                .expectNext(entityDto)
                .verifyComplete();

        verify(virtualEntityRepository).findByName(entityName);
        verify(virtualEntityMapper).toDto(entity);
    }

    @Test
    void getEntityByName_WhenNotFound_ShouldReturnEmpty() {
        String entityName = "NonExistentEntity";
        when(virtualEntityRepository.findByName(entityName)).thenReturn(Mono.empty());

        StepVerifier.create(virtualEntityService.getEntityByName(entityName))
                .verifyComplete();

        verify(virtualEntityRepository).findByName(entityName);
        verify(virtualEntityMapper, never()).toDto(any());
    }

    @Test
    void getEntitySchema_ShouldReturnSchema() {
        String entityName = "TestEntity";
        when(virtualEntityRepository.findByNameAndActive(entityName, true)).thenReturn(Mono.just(entity));
        when(virtualEntityMapper.toDto(entity)).thenReturn(entityDto);
        when(virtualEntityFieldService.getFieldsByEntityId(entityId)).thenReturn(Flux.just(fieldDto));

        StepVerifier.create(virtualEntityService.getEntitySchema(entityName))
                .expectNextMatches(schema -> 
                    schema.getEntity().equals(entityDto) && 
                    schema.getFields().size() == 1 && 
                    schema.getFields().get(0).equals(fieldDto))
                .verifyComplete();

        verify(virtualEntityRepository).findByNameAndActive(entityName, true);
        verify(virtualEntityMapper).toDto(entity);
        verify(virtualEntityFieldService).getFieldsByEntityId(entityId);
    }

    @Test
    void getEntitySchema_WhenNotFound_ShouldReturnEmpty() {
        String entityName = "NonExistentEntity";
        when(virtualEntityRepository.findByNameAndActive(entityName, true)).thenReturn(Mono.empty());

        StepVerifier.create(virtualEntityService.getEntitySchema(entityName))
                .verifyComplete();

        verify(virtualEntityRepository).findByNameAndActive(entityName, true);
        verify(virtualEntityMapper, never()).toDto(any());
        verify(virtualEntityFieldService, never()).getFieldsByEntityId(any());
    }

    @Test
    void createEntity_ShouldCreateAndReturnEntity() {
        when(virtualEntityRepository.existsByName("TestEntity")).thenReturn(Mono.just(false));
        when(virtualEntityMapper.toEntity(entityDto)).thenReturn(entity);
        when(virtualEntityRepository.save(any(VirtualEntity.class))).thenReturn(Mono.just(entity));
        when(virtualEntityMapper.toDto(entity)).thenReturn(entityDto);

        StepVerifier.create(virtualEntityService.createEntity(entityDto))
                .expectNext(entityDto)
                .verifyComplete();

        verify(virtualEntityRepository).existsByName("TestEntity");
        verify(virtualEntityMapper).toEntity(entityDto);
        verify(virtualEntityRepository).save(any(VirtualEntity.class));
        verify(virtualEntityMapper).toDto(entity);
    }

    @Test
    void createEntity_WhenNameExists_ShouldReturnError() {
        when(virtualEntityRepository.existsByName("TestEntity")).thenReturn(Mono.just(true));

        StepVerifier.create(virtualEntityService.createEntity(entityDto))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException && 
                    throwable.getMessage().contains("Entity with name"))
                .verify();

        verify(virtualEntityRepository).existsByName("TestEntity");
        verify(virtualEntityMapper, never()).toEntity(any());
        verify(virtualEntityRepository, never()).save(any());
    }

    @Test
    void updateEntity_ShouldUpdateAndReturnEntity() {
        when(virtualEntityRepository.findById(entityId)).thenReturn(Mono.just(entity));
        when(virtualEntityMapper.toEntity(entityDto)).thenReturn(entity);
        when(virtualEntityRepository.save(any(VirtualEntity.class))).thenReturn(Mono.just(entity));
        when(virtualEntityMapper.toDto(entity)).thenReturn(entityDto);

        StepVerifier.create(virtualEntityService.updateEntity(entityId, entityDto))
                .expectNext(entityDto)
                .verifyComplete();

        verify(virtualEntityRepository).findById(entityId);
        verify(virtualEntityMapper).toEntity(entityDto);
        verify(virtualEntityRepository).save(any(VirtualEntity.class));
        verify(virtualEntityMapper).toDto(entity);
    }

    @Test
    void updateEntity_WhenNotFound_ShouldReturnError() {
        when(virtualEntityRepository.findById(entityId)).thenReturn(Mono.empty());

        StepVerifier.create(virtualEntityService.updateEntity(entityId, entityDto))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException && 
                    throwable.getMessage().contains("Entity with ID"))
                .verify();

        verify(virtualEntityRepository).findById(entityId);
        verify(virtualEntityMapper, never()).toEntity(any());
        verify(virtualEntityRepository, never()).save(any());
    }

    @Test
    void deleteEntity_ShouldDeleteEntity() {
        when(virtualEntityRepository.findById(entityId)).thenReturn(Mono.just(entity));
        when(virtualEntityRecordService.deleteRecordsByEntityId(entityId)).thenReturn(Mono.empty());
        when(virtualEntityFieldService.deleteFieldsByEntityId(entityId)).thenReturn(Mono.empty());
        when(virtualEntityRepository.deleteById(entityId)).thenReturn(Mono.empty());

        StepVerifier.create(virtualEntityService.deleteEntity(entityId))
                .verifyComplete();

        verify(virtualEntityRepository).findById(entityId);
        verify(virtualEntityRecordService).deleteRecordsByEntityId(entityId);
        verify(virtualEntityFieldService).deleteFieldsByEntityId(entityId);
        verify(virtualEntityRepository).deleteById(entityId);
    }

    @Test
    void deleteEntity_WhenNotFound_ShouldReturnError() {
        when(virtualEntityRepository.findById(entityId)).thenReturn(Mono.empty());

        StepVerifier.create(virtualEntityService.deleteEntity(entityId))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException && 
                    throwable.getMessage().contains("Entity with ID"))
                .verify();

        verify(virtualEntityRepository).findById(entityId);
        verify(virtualEntityRecordService, never()).deleteRecordsByEntityId(any());
        verify(virtualEntityFieldService, never()).deleteFieldsByEntityId(any());
        verify(virtualEntityRepository, never()).deleteById(any(UUID.class));
    }
}
