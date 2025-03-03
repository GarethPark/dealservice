package com.example.dealservice.entities

import com.example.dealservice.enums.DealStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.Currency

@Entity
@Table(name = "deals")
data class Deal (

    @Id
    val id: Long = 0,

    @Column
    val codeName: String,

    @Column
    val description: String,

    @Column
    val status: DealStatus,

    @Column
    val exclusivity: Boolean,

    @Column
    val highlyConfidential: Boolean,

    @Column
    val currency: com.example.dealservice.enums.Currency

)


