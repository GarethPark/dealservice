package com.example.dealservice.unit.service

import com.example.dealservice.dtos.CreateDealRequest
import com.example.dealservice.entities.Deal
import com.example.dealservice.enums.Currency
import com.example.dealservice.enums.DealStatus
import com.example.dealservice.repository.DealRepository
import com.example.dealservice.service.DealService
import jakarta.validation.ConstraintViolation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import javax.validation.ConstraintViolation
import javax.validation.Validator

class DealServiceTests {

    private lateinit var dealRepository: DealRepository
    private lateinit var validator: Validator
    private lateinit var dealService: DealService

    @BeforeEach
    fun setup() {
        dealRepository = mock()
        validator = mock()

        dealService = DealService(
            dealRepository = dealRepository,
            validator = validator
        )
    }

    @Test
    fun `should create a new deal with valid payload`() {
        // given
        val createDealRequest = CreateDealRequest(
            codeName = "Project Alpha",
            description = "Strategic acquisition deal",
            status = DealStatus.DRAFT,
            exclusivity = true,
            highlyConfidential = true,
            currency = Currency.USD
        )

        val savedDeal = Deal(
            id = 1L,
            codeName = createDealRequest.codeName,
            description = createDealRequest.description,
            status = createDealRequest.status,
            exclusivity = createDealRequest.exclusivity,
            highlyConfidential = createDealRequest.highlyConfidential,
            currency = createDealRequest.currency
        )

        // Configure mocks
        `when`(validator.validate(any())).thenReturn(emptySet<ConstraintViolation<Any>>())
        `when`(dealRepository.save(any(Deal::class.java))).thenReturn(savedDeal)

        // when
        val result = dealService.createDeal(createDealRequest)

        // then
        assertNotNull(result)
        assertEquals(1L, result.id)

        // verify interactions
        verify(dealRepository).save(any(Deal::class.java))
    }
}