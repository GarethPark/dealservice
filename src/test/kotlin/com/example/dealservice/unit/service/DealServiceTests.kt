package com.example.dealservice.unit.service

import com.example.dealservice.dtos.*
import com.example.dealservice.repositories.DealCriteriaFactory
import com.example.dealservice.entities.Deal
import com.example.dealservice.enums.Currency
import com.example.dealservice.enums.DealStatus
import com.example.dealservice.integration.testutils.MockitoHelper.anyObject
import com.example.dealservice.repositories.DealRepository
import com.example.dealservice.service.DealService
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.validation.ConstraintViolation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.lang.IllegalArgumentException
import java.time.Clock
import javax.xml.validation.Validator

class DealServiceTests {

    private lateinit var dealRepository: DealRepository
    private lateinit var validator: Validator
    private lateinit var dealService: DealService
    private lateinit var dealCriteriaFactory: DealCriteriaFactory
    private lateinit var clock: Clock
    private lateinit var path: Path<*>
    private lateinit var predicate: Predicate
    private val maxPageSize = 100


    @BeforeEach
    fun setup() {
        dealRepository = mock()
        validator = mock()
        dealCriteriaFactory = mock()
        path = mock()
        clock = mock()
        predicate = mock()
        //TODO Clock

        dealService = DealService(
            dealRepository = dealRepository,
            dealCriteriaFactory = dealCriteriaFactory,
            maxPageSize = maxPageSize
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
    fun `should throw an exception when creating deals with invalid data`(){

        val createDealRequest = CreateDealRequest(
            codeName = "",
            description = "",
            status = DealStatus.DRAFT,
            currency = Currency.USD,
            highlyConfidential = false,
            exclusivity = false
        )

        assertThrows(IllegalArgumentException::class.java){
            dealService.createDeal(createDealRequest)
        }
    }

    @Test
    fun searchForDeals_shouldAllowFiltering(){
        `when`(dealCriteriaFactory.getPath(anyObject(), anyObject())).thenReturn(path)
        `when`(
            dealCriteriaFactory.getPredicate(
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
                anyObject(),
            )
        ).thenReturn(predicate)
        `when`(dealRepository.findAll(anyObject<Specification<Deal>>(), anyObject<Pageable>())).thenReturn(
            PageImpl(
                emptyList()
            )
        )

        val dealSearchDTO = DealSearchDTO(
            filter = FilterGroup(
                operator = LogicalOperator.OR,
                conditions = listOf(
                    FilterCondition(
                        field = FilterField.HIGHLY_CONFIDENTIAL,
                        operator = ComparisonOperator.EQ,
                        value = "AA"),
                    FilterCondition(
                        field = FilterField.CODE_NAME,
                        operator = ComparisonOperator.EQ,
                        value = "EMEA"
                    ),
                    FilterCondition(
                        field = FilterField.STATUS,
                        operator = ComparisonOperator.EQ,
                        value = "CLOSED"
                    )
                ),
                groups = null
            ),
            size = 10,
            page = 0
        )

        val deals = dealService.searchForDealsV2(dealSearchDTO)
        assertThat(deals).isNotNull
    }
    @Test
    fun searchForDealsWithoutFilter_shouldFindAllDeals(){
        `when`(dealRepository.findAll(anyObject<Pageable>())).thenReturn(
            PageImpl(
                emptyList()
            )
        )

        val dealSearchDTO = DealSearchDTO(
            size = 10,
            page = 0
        )

        val deals = dealService.searchForDealsV2(dealSearchDTO)
        assertThat(deals).isNotNull
    }
}