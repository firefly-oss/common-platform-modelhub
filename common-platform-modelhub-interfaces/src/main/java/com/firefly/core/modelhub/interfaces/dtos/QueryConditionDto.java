package com.firefly.core.modelhub.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO representing a single query condition.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Query condition")
public class QueryConditionDto {

    @Schema(description = "Field key to query on", example = "firstName", required = true)
    private String field;

    @Schema(description = "Operator for the condition", example = "eq", required = true)
    private String operator;

    @Schema(description = "Value to compare against", example = "John")
    private Object value;

    /**
     * Enum of supported operators for query conditions.
     */
    public enum Operator {
        EQ("eq"),           // equals
        NEQ("neq"),         // not equals
        GT("gt"),           // greater than
        GTE("gte"),         // greater than or equal
        LT("lt"),           // less than
        LTE("lte"),         // less than or equal
        CONTAINS("contains"), // contains substring (for strings) or element (for arrays)
        STARTS_WITH("startsWith"), // starts with (for strings)
        ENDS_WITH("endsWith"), // ends with (for strings)
        IN("in"),           // in a list of values
        NOT_IN("notIn"),    // not in a list of values
        IS_NULL("isNull"),  // is null
        IS_NOT_NULL("isNotNull"); // is not null

        private final String value;

        Operator(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Operator fromString(String value) {
            for (Operator operator : Operator.values()) {
                if (operator.value.equalsIgnoreCase(value)) {
                    return operator;
                }
            }
            throw new IllegalArgumentException("Unknown operator: " + value);
        }
    }
}