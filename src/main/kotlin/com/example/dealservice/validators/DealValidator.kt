package com.example.dealservice.validators

import com.example.dealservice.entities.Deal
import org.springframework.validation.Errors
import org.springframework.validation.Validator

class DealValidator : Validator {
    override fun supports(clazz: Class<*>): Boolean {
        // This ensures the validator supports the Deal class (or CreateDealRequestDTO)
        return Deal::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val deal = target as Deal

        if (deal.codeName.isBlank()){
            errors.rejectValue("codeName", "deal.codeName.blank", "Deal CodeName cannot be blank")
        }
        TODO("Not yet implemented")
    }
}