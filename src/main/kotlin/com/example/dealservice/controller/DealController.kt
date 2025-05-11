package com.example.dealservice.controller

import com.example.dealservice.dtos.CreateDealRequest
import com.example.dealservice.dtos.DealSearchRequest
import com.example.dealservice.entities.Deal
import com.example.dealservice.service.DealService
import org.springframework.beans.factory.annotation.Autowired
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
    fun searchDeals(@RequestBody searchRequest: DealSearchRequest): ResponseEntity<List<Deal>> {
        val deals = dealService.searchDeals(searchRequest)
        return ResponseEntity.ok(deals)
    }
} 