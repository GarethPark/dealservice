package com.example.dealservice.service

import com.example.dealservice.dtos.CreateDealRequest
import com.example.dealservice.dtos.DealFilter
import com.example.dealservice.dtos.DealSearchRequest
import com.example.dealservice.dtos.FilterOperator
import com.example.dealservice.entities.Deal
import com.example.dealservice.repositories.DealRepository
import com.example.dealservice.validators.DealValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.Specification
import javax.xml.validation.Validator

class DealService (@Autowired private val dealRepository: DealRepository,
    @Autowired private val validator: Validator
) {

    private val dealValidator = DealValidator()
    
    fun createDeal(createDealRequest: CreateDealRequest) : Deal{
        var deal = Deal(
            codeName = createDealRequest.codeName,
            description = createDealRequest.description,
            status = createDealRequest.status,
            exclusivity = createDealRequest.exclusivity,
            highlyConfidential = createDealRequest.highlyConfidential,
            currency = createDealRequest.currency
        )

        validateDeal(deal)

        return dealRepository.save(deal)
    }

    fun searchDeals(searchRequest: DealSearchRequest): List<Deal> {
        val specification = searchRequest.filters.fold(Specification.where(null)) { spec, filter ->
            spec.and(createSpecification(filter))
        }
        return dealRepository.findAll(specification)
    }

    private fun createSpecification(filter: DealFilter): Specification<Deal> {
        return Specification { root, query, criteriaBuilder ->
            when (filter.operator) {
                FilterOperator.EQUALS -> criteriaBuilder.equal(root.get<Any>(filter.field), filter.value)
                FilterOperator.CONTAINS -> criteriaBuilder.like(
                    root.get(filter.field).as(String::class.java),
                    "%${filter.value}%"
                )
                FilterOperator.GREATER_THAN -> criteriaBuilder.greaterThan(
                    root.get(filter.field).as(Comparable::class.java),
                    filter.value as Comparable<Any>
                )
                FilterOperator.LESS_THAN -> criteriaBuilder.lessThan(
                    root.get(filter.field).as(Comparable::class.java),
                    filter.value as Comparable<Any>
                )
                FilterOperator.IN -> {
                    val values = (filter.value as List<*>).toTypedArray()
                    root.get<Any>(filter.field).`in`(*values)
                }
            }
        }
    }

    private fun validateDeal(deal: Deal){
        val errors = org.springframework.validation.BeanPropertyBindingResult(deal, "deal")
        dealValidator.validate(deal, errors)

        if (errors.hasErrors()) {
            throw IllegalArgumentException("Deal validation failed: ${errors.allErrors.joinToString(", ") { it.defaultMessage ?: "" }}")
        }
    }
}