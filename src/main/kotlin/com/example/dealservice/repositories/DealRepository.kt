package com.example.dealservice.repositories

import com.example.dealservice.entities.Deal
import org.springframework.data.jpa.repository.JpaRepository

interface DealRepository : JpaRepository<Deal, Long> {
    fun findByName(name: String): List<Deal>
}