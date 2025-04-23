package com.catalis.core.modelhub.interfaces.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO representing a group of query conditions that can be nested.
 * This allows for more complex queries with multiple levels of AND/OR logic.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Query condition group")
public class QueryGroupDto {

    @Schema(description = "List of conditions in this group")
    @Builder.Default
    private List<QueryConditionDto> conditions = new ArrayList<>();

    @Schema(description = "List of nested condition groups")
    @Builder.Default
    private List<QueryGroupDto> groups = new ArrayList<>();

    @Schema(description = "Logical operator to combine conditions and groups", example = "AND", defaultValue = "AND")
    @Builder.Default
    private QueryDto.LogicalOperator logicalOperator = QueryDto.LogicalOperator.AND;

    /**
     * Add a condition to this group.
     *
     * @param field    the field to query on
     * @param operator the operator to use
     * @param value    the value to compare against
     * @return this group for chaining
     */
    public QueryGroupDto addCondition(String field, String operator, Object value) {
        conditions.add(QueryConditionDto.builder()
                .field(field)
                .operator(operator)
                .value(value)
                .build());
        return this;
    }

    /**
     * Add a nested group to this group.
     *
     * @param group the group to add
     * @return this group for chaining
     */
    public QueryGroupDto addGroup(QueryGroupDto group) {
        groups.add(group);
        return this;
    }

    /**
     * Create a new nested group with the specified logical operator and add it to this group.
     *
     * @param logicalOperator the logical operator for the new group
     * @return the newly created group for chaining
     */
    public QueryGroupDto createGroup(QueryDto.LogicalOperator logicalOperator) {
        QueryGroupDto group = new QueryGroupDto();
        group.setLogicalOperator(logicalOperator);
        groups.add(group);
        return group;
    }
}