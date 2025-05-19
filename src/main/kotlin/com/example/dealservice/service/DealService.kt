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

    fun searchForDealsV2(dealSearchDTO: DealSearchDTO): Page<Deal> {
        val pageable = PageRequest.of(dealSearchDTO.page, dealSearchDTO.size)
        
        if (dealSearchDTO.filter == null) {
            return dealRepository.findAll(pageable)
        }

        val spec = 
    }

    fun convertToSpec(filter: FilterGroup): Specification<Deal> = 
        Specification({ root, query, criteriaBuilder<*>?, cb: CriteriaBuilder ->
            val predicates: MutableList<Predicate> = arrayListOf()

            if(filter.conditions != null) {
                for (predicate in filter.conditions) {
                    predicates.add(buildPredicate(predicate, root, cb))
                }
            }

            if(filter.groups != null) {
                for (group in filter.groups) {
                    convertToSpec(group).toPredicate(root, query, cb) 
                    ?.apply {predicates.add(this)}
                }
            }

            query?.distinct(true)

            return@Specification when (filter.operator) {
                LogicOperator.AND -> cb.and(*predicates.toTypedArray<Predicate>())
                LogicOperator.OR -> cb.or(*predicates.toTypedArray<Predicate>())
            }        
        })
    }

    private fun buildPredicate(condition: FilterCondition, root: Root<Deal>, cb: CriteriaBuilder): Predicate {
        val path: Path<*> = dealCriteriaFactory.gethPath(condition.field, root)
        return dealCriteriaFactory.getPredicate(condition.field, cb, path, condition.operator, condition.value)
    }


    private fun validateDeal(deal: Deal){
        val errors = org.springframework.validation.BeanPropertyBindingResult(deal, "deal")
        dealValidator.validate(deal, errors)

        if (errors.hasErrors()) {
            throw IllegalArgumentException("Deal validation failed: ${errors.allErrors.joinToString(", ") { it.defaultMessage ?: "" }}")
        }
    }
}