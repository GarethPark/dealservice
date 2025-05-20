package com.example.dealservice.repositories

import com.example.dealservice.entities.Deal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface DealRepository : JpaRepository<Deal, Long>, JpaSpecificationExecutor<Deal> {
    fun findByCodeName(codeName: String): List<Deal>
}