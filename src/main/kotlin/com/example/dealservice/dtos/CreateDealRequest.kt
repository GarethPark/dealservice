package com.example.dealservice.dtos

import com.example.dealservice.enums.DealStatus
import java.util.Currency

data class CreateDealRequest (
    val id: Long? = null,
    val codeName: String,
    val description: String,
    val status: DealStatus,
    val exclusivity: Boolean,
    val highlyConfidential: Boolean,
    val currency: com.example.dealservice.enums.Currency
)
