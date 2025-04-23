CREATE TABLE virtual_entity (
    id UUID PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL,
    description TEXT,
    version INTEGER,
    active BOOLEAN DEFAULT true
);

CREATE TABLE virtual_entity_field (
    id UUID PRIMARY KEY,
    entity_id UUID NOT NULL,
    field_key VARCHAR NOT NULL,
    field_label VARCHAR,
    field_type VARCHAR, -- e.g., 'string', 'number', 'boolean', etc.
    required BOOLEAN,
    options JSONB,
    order_index INTEGER,
    FOREIGN KEY (entity_id) REFERENCES virtual_entity(id)
);

CREATE TABLE virtual_entity_record (
    id UUID PRIMARY KEY,
    entity_id UUID NOT NULL,
    payload JSONB NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (entity_id) REFERENCES virtual_entity(id)
);