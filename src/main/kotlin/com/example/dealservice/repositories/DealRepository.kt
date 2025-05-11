package com.example.dealservice.repositories

import com.example.dealservice.entities.Deal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface DealRepository : JpaRepository<Deal, Long>, JpaSpecificationExecutor<Deal> {
    fun findByName(name: String): List<Deal>
}