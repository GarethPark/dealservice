package com.example.dealservice.repositories

import org.springframework.stereotype.Component
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import jakarta.persistence.criteria.Path
import com.example.dealservice.entities.Deal
import com.example.dealservice.dtos.FilterField
import com.example.dealservice.dtos.ComparisonOperator

@Component
interface DealCriteriaFactory {
    fun getPath(field: FilterField, root: Root<Deal>): Path<*>

    fun getPredicate(
        field: FilterField,
        cb: CriteriaBuilder,
        path: Path<*>,
        operator: ComparisonOperator,
        value: Any?    
    ): Predicate
}