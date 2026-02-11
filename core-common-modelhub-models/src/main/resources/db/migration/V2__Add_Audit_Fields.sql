-- Add audit fields to virtual_entity
ALTER TABLE virtual_entity
ADD COLUMN created_by VARCHAR,
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_by VARCHAR,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Add audit fields to virtual_entity_field
ALTER TABLE virtual_entity_field
ADD COLUMN created_by VARCHAR,
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_by VARCHAR,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Update virtual_entity_record to include created_by and updated_by
ALTER TABLE virtual_entity_record
ADD COLUMN created_by VARCHAR,
ADD COLUMN updated_by VARCHAR;

-- Add indexes for better performance
CREATE INDEX idx_virtual_entity_name ON virtual_entity(name);
CREATE INDEX idx_virtual_entity_field_entity_id ON virtual_entity_field(entity_id);
CREATE INDEX idx_virtual_entity_record_entity_id ON virtual_entity_record(entity_id);