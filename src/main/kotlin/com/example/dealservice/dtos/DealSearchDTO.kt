package com.example.dealservice.dtos

data class DealSearchDTO(
    val filter: FilterGroup? = null,
    val size: Int,
    val page: Int 
)

data class FilterGroup(
    val operator: LogicalOperator.AND,
    val conditions: List<FilterCondition>? = null,
    val groups: List<FilterGroup>? = null
)

data class FilterCondition(
    val field: FilterField,
    val operator: ComparisonOperator,
    val value: Any? = null
)

enum class LogicalOperator {
    AND, OR
}

enum class ComparisonOperator {
    EQ,
    IN,
    NOT_NUL
}

emum FilterField {
    CODE_NAME,
    STATUS,
    EXCLUSIVITY,
    HIGHLY_CONFIDENTIAL,
    CURRENCY
}
