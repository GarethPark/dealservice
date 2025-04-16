package com.example.dealservice.dtos

import com.example.dealservice.enums.DealStatus
import jakarta.validation.constraints.NotBlank
import java.util.Currency
import jakarta.validation.constraints.Size

data class CreateDealRequest (
    val id: Long? = null,
    @field:NotBlank(message = "code name cannot be empty")
    @field:Size(min=1, max=100, message="CodeName must be between 1 and 10 characters" )
    val codeName: String,
    @field:NotBlank(message = "description cannot be blank")
    @field:Size(min = 1, max = 500, message = "Description must be between 1 and 500 characters")
    val description: String,
    val status: DealStatus,
    val exclusivity: Boolean,
    val highlyConfidential: Boolean,
    val currency: com.example.dealservice.enums.Currency
)
