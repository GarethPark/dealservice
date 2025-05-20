package com.example.dealservice.unit.service

import DealCriteriaFactory
import com.example.dealservice.dtos.CreateDealRequest
import com.example.dealservice.entities.Deal
import com.example.dealservice.enums.Currency
import com.example.dealservice.enums.DealStatus
import com.example.dealservice.repositories.DealRepository
import com.example.dealservice.service.DealService
import jakarta.validation.ConstraintViolation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.lang.IllegalArgumentException
import javax.xml.validation.Validator

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
        //`when`(validator.validate(any())).thenReturn(emptySet<ConstraintViolation<Any>>())
        `when`(dealRepository.save(any(Deal::class.java))).thenReturn(savedDeal)

        // when
        val result = dealService.createDeal(createDealRequest)

        // then
        assertNotNull(result)
        assertEquals(1L, result.id)

        // verify interactions
        verify(dealRepository).save(any(Deal::class.java))
    }

    @Test
    fun `should thow an exception when creating deals with invalid data`(){

        val createDealRequest = CreateDealRequest(
            codeName = "",
            description = "",
            status = DealStatus.DRAFT,
            currency = Currency.USD,
            highlyConfidential = false,
            exclusivity = false
        )

        val violation = mock(ConstraintViolation::class.java)
        `when`(validator.validate(any()))

        assertThrows(IllegalArgumentException::class.java){
            dealService.createDeal(createDealRequest)
        }

    }
}