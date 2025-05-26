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

    @Column(name = "codename")
    val codename: String,

    @Column(name = "description")
    val description: String,

    @Column(name = "status")
    val status: DealStatus,

    @Column(name = "exclusivity")
    val exclusivity: Boolean,

    @Column(name = "highlyconfidential")
    val highlyconfidential: Boolean,

    @Column(name = "currency")
    val currency: com.example.dealservice.enums.Currency

)


