package com.example.dealservice.service

import com.example.dealservice.dtos.CreateDealRequest
import com.example.dealservice.entities.Deal
import com.example.dealservice.repositories.DealRepository
import com.example.dealservice.validators.DealValidator
import org.springframework.beans.factory.annotation.Autowired
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

    private fun validateDeal(deal: Deal){
        val errors = org.springframework.validation.BeanPropertyBindingResult(deal, "deal")
        dealValidator.validate(deal, errors)

        if (errors.hasErrors()) {
            throw IllegalArgumentException("Deal validation failed: ${errors.allErrors.joinToString(", ") { it.defaultMessage ?: "" }}")
        }
    }
}