package com.example.dealservice.dtos

data class DealSearchDTO(
    val filter: FilterGroup? = null,
    val size: Int,
    val page: Int 
)

data class FilterGroup(
    val operator: LogicalOperator = LogicalOperator.AND,
    val conditions: List<FilterCondition>? = null,
    val groups: List<FilterGroup>? = null
)

data class FilterCondition(
    val field: FilterField,
    val operator: ComparisonOperator,
    val value: Any? = null
)

enum class FilterField {
    CODE_NAME,
    STATUS,
    EXCLUSIVITY,
    HIGHLY_CONFIDENTIAL,
    CURRENCY
}

enum class ComparisonOperator {
    EQ,
    IN,
    NOT_NULL
}

enum class LogicalOperator {
    AND, OR
}
