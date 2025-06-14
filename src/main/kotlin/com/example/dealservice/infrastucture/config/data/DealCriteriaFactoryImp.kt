package com.example.dealservice.infrastucture.config.data

import com.example.dealservice.dtos.ComparisonOperator
import com.example.dealservice.dtos.FilterField
import com.example.dealservice.entities.Deal
import com.example.dealservice.repositories.DealCriteriaFactory
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name= ["spring.datasource.driver-class-name"], havingValue = "oracle.jdbc.OracleDriver")
class OracleDealCriteriaFactoryImp: DealCriteriaFactory{

    override fun getPath(field: FilterField, root: Root<Deal>): Path<*> {
        return path(field, root)
    }

    override fun getPredicate(
        field: FilterField,
        cb: CriteriaBuilder,
        path: Path<*>,
        operator: ComparisonOperator,
        value: Any?
    ): Predicate{
        return when (field){
            FilterField.HIGHLY_CONFIDENTIAL -> basicPredicate(operator, cb, path, value)
            FilterField.EXCLUSIVITY -> basicPredicate(operator, cb, path, value)
            FilterField.CODE_NAME -> basicPredicate(operator, cb, path, value)
            FilterField.STATUS -> basicPredicate(operator, cb, path, value)
            FilterField.CURRENCY -> basicPredicate(operator, cb, path, value)
        }
    }

    //TODO JSON ArrayPredicate
}
@Component
@ConditionalOnProperty(name = ["spring.datasource.driver-class-name"], havingValue = "org.h2.Driver")
class H2DealCriteriaFactory: DealCriteriaFactory{

    override fun getPath(field: FilterField, root: Root<Deal>): Path<*> {
        return path(field, root)
    }

    override fun getPredicate(
        field: FilterField,
        cb: CriteriaBuilder,
        path: Path<*>,
        operator: ComparisonOperator,
        value: Any?
    ): Predicate {
        return when (field) {
            FilterField.HIGHLY_CONFIDENTIAL -> basicPredicate(operator, cb, path, value)
            FilterField.EXCLUSIVITY -> basicPredicate(operator, cb, path, value)
            FilterField.CODE_NAME -> basicPredicate(operator, cb, path, value)
            FilterField.STATUS -> basicPredicate(operator, cb, path, value)
            FilterField.CURRENCY -> basicPredicate(operator, cb, path, value)
        }
    }
}

private fun path(
    field: FilterField,
    root: Root<Deal>
): Path<*> {
    return when (field) {
        FilterField.CODE_NAME -> root.get<String>("codename")
        FilterField.STATUS -> root.get<String>("status")
        FilterField.CURRENCY -> root.get<String>("currency")
        FilterField.EXCLUSIVITY -> root.get<Boolean>("exclusivity")
        FilterField.HIGHLY_CONFIDENTIAL -> root.get<Boolean>("highlyconfidential")
    }
}

private fun basicPredicate(
    operator: ComparisonOperator,
    cb: CriteriaBuilder,
    path: Path<*>,
    value: Any?
): Predicate {
    System.out.println("DEBUG - operator = " + operator)
    System.out.println("DEBUG - value = " + value + " (type: " + (value?.javaClass?.name ?: "null") + ")")
    System.out.println("DEBUG - path = " + path + " (type: " + path.javaType.name + ")")
    
    return when (operator) {
        ComparisonOperator.EQ -> {
            System.out.println("DEBUG - Creating EQ predicate with types: " + path.javaType.name + " and " + (value?.javaClass?.name ?: "null"))
            cb.equal(path, value)
        }
        ComparisonOperator.IN -> path.`in`(*(value as ArrayList<*>).toTypedArray())
        ComparisonOperator.NOT_NULL -> cb.isNotNull(path)
    }
}


