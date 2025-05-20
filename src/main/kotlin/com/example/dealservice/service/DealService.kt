package com.example.dealservice.service


import com.example.dealservice.repositories.DealCriteriaFactory
import com.example.dealservice.dtos.*
import com.example.dealservice.entities.Deal
import com.example.dealservice.repositories.DealRepository
import com.example.dealservice.validators.DealValidator
import jakarta.persistence.criteria.*
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class DealService (
    private val dealRepository: DealRepository,
    private val dealCriteriaFactory: DealCriteriaFactory
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

    fun searchForDealsV2(dealSearchDTO: DealSearchDTO): Page<Deal> {
        val pageable = PageRequest.of(dealSearchDTO.page, dealSearchDTO.size)
        
        if (dealSearchDTO.filter == null) {
            return dealRepository.findAll(pageable)
        }

        val spec = convertToSpec(dealSearchDTO.filter).and { root: Root<Deal>, query: CriteriaQuery<*>?, cb: CriteriaBuilder ->
            dealCriteriaFactory.getPredicate(
                FilterField.CODE_NAME,
                cb,
                dealCriteriaFactory.getPath(FilterField.CODE_NAME, root),
                ComparisonOperator.EQ,
                "TEST_CODENAME"
            )
        }

        val deals = dealRepository.findAll(spec, pageable)

        return deals 
    }

    private fun convertToSpec(filter: FilterGroup): Specification<Deal> =
        Specification({ root: Root<Deal>, query: CriteriaQuery<*>?, cb: CriteriaBuilder ->
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
                LogicalOperator.AND -> cb.and(*predicates.toTypedArray<Predicate>())
                LogicalOperator.OR -> cb.or(*predicates.toTypedArray<Predicate>())
            }
        })


    private fun buildPredicate(condition: FilterCondition, root: Root<Deal>, cb: CriteriaBuilder): Predicate {
        val path: Path<*> = dealCriteriaFactory.getPath(condition.field, root)
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