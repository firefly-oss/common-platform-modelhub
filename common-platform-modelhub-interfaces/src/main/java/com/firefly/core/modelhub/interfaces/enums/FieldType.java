package com.firefly.core.modelhub.interfaces.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the supported field types for virtual entity fields.
 */
@Getter
@RequiredArgsConstructor
public enum FieldType {
    STRING("string"),
    NUMBER("number"),
    INTEGER("integer"),
    BOOLEAN("boolean"),
    DATE("date"),
    DATETIME("datetime"),
    EMAIL("email"),
    PHONE("phone"),
    URL("url"),
    ENUM("enum"),
    OBJECT("object"),
    ARRAY("array"),
    REFERENCE("reference");

    @JsonValue
    private final String value;

    /**
     * Converts a string value to the corresponding FieldType enum.
     *
     * @param value the string value to convert
     * @return the corresponding FieldType enum, or null if not found
     */
    public static FieldType fromValue(String value) {
        for (FieldType type : FieldType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}