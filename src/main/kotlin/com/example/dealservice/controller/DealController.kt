package com.example.dealservice.controller

import com.example.dealservice.dtos.CreateDealRequest
import com.example.dealservice.dtos.DealSearchDTO
import com.example.dealservice.entities.Deal
import com.example.dealservice.service.DealService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/deals")
class DealController(@Autowired private val dealService: DealService) {

    @PostMapping
    fun createDeal(@RequestBody createDealRequest: CreateDealRequest): ResponseEntity<Deal> {
        val deal = dealService.createDeal(createDealRequest)
        return ResponseEntity.ok(deal)
    }

    @PostMapping("/search")
    fun searchDeals(@RequestBody searchRequest: DealSearchDTO): ResponseEntity<Page<Deal>> {
        val deals = dealService.searchForDealsV2(searchRequest)
        return ResponseEntity.ok(deals)
    }

    @GetMapping("/jenkins-test")
    fun jenkinsTest(): String = "Jenkins deploy works new endpoint"
} 