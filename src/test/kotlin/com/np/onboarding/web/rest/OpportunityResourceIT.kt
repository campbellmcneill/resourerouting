package com.np.onboarding.web.rest

import com.np.onboarding.ResourceroutingApp
import com.np.onboarding.domain.Opportunity
import com.np.onboarding.repository.OpportunityRepository
import com.np.onboarding.repository.search.OpportunitySearchRepository
import com.np.onboarding.service.OpportunityService
import com.np.onboarding.service.mapper.OpportunityMapper
import com.np.onboarding.web.rest.errors.ExceptionTranslator
import javax.persistence.EntityManager
import kotlin.test.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator

/**
 * Integration tests for the [OpportunityResource] REST controller.
 *
 * @see OpportunityResource
 */
@SpringBootTest(classes = [ResourceroutingApp::class])
class OpportunityResourceIT {

    @Autowired
    private lateinit var opportunityRepository: OpportunityRepository

    @Mock
    private lateinit var opportunityRepositoryMock: OpportunityRepository

    @Autowired
    private lateinit var opportunityMapper: OpportunityMapper

    @Mock
    private lateinit var opportunityServiceMock: OpportunityService

    @Autowired
    private lateinit var opportunityService: OpportunityService

    /**
     * This repository is mocked in the com.np.onboarding.repository.search test package.
     *
     * @see com.np.onboarding.repository.search.OpportunitySearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockOpportunitySearchRepository: OpportunitySearchRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restOpportunityMockMvc: MockMvc

    private lateinit var opportunity: Opportunity

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val opportunityResource = OpportunityResource(opportunityService)
        this.restOpportunityMockMvc = MockMvcBuilders.standaloneSetup(opportunityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        opportunity = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createOpportunity() {
        val databaseSizeBeforeCreate = opportunityRepository.findAll().size

        // Create the Opportunity
        val opportunityDTO = opportunityMapper.toDto(opportunity)
        restOpportunityMockMvc.perform(
            post("/api/opportunities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(opportunityDTO))
        ).andExpect(status().isCreated)

        // Validate the Opportunity in the database
        val opportunityList = opportunityRepository.findAll()
        assertThat(opportunityList).hasSize(databaseSizeBeforeCreate + 1)
        val testOpportunity = opportunityList[opportunityList.size - 1]
        assertThat(testOpportunity.opportunityTitle).isEqualTo(DEFAULT_OPPORTUNITY_TITLE)
        assertThat(testOpportunity.opportunityDescription).isEqualTo(DEFAULT_OPPORTUNITY_DESCRIPTION)
        assertThat(testOpportunity.weeklyTimeCommitment).isEqualTo(DEFAULT_WEEKLY_TIME_COMMITMENT)
        assertThat(testOpportunity.duration).isEqualTo(DEFAULT_DURATION)

        // Validate the Opportunity in Elasticsearch
    }

    @Test
    @Transactional
    fun createOpportunityWithExistingId() {
        val databaseSizeBeforeCreate = opportunityRepository.findAll().size

        // Create the Opportunity with an existing ID
        opportunity.id = 1L
        val opportunityDTO = opportunityMapper.toDto(opportunity)

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpportunityMockMvc.perform(
            post("/api/opportunities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(opportunityDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Opportunity in the database
        val opportunityList = opportunityRepository.findAll()
        assertThat(opportunityList).hasSize(databaseSizeBeforeCreate)

        // Validate the Opportunity in Elasticsearch
        verify(mockOpportunitySearchRepository, times(0)).save(opportunity)
    }

    @Test
    @Transactional
    fun getAllOpportunities() {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity)

        // Get all the opportunityList
        restOpportunityMockMvc.perform(get("/api/opportunities?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opportunity.id?.toInt())))
            .andExpect(jsonPath("$.[*].opportunityTitle").value(hasItem(DEFAULT_OPPORTUNITY_TITLE)))
            .andExpect(jsonPath("$.[*].opportunityDescription").value(hasItem(DEFAULT_OPPORTUNITY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].weeklyTimeCommitment").value(hasItem(DEFAULT_WEEKLY_TIME_COMMITMENT.toInt())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toInt())))
    }

    @Suppress("unchecked")
    fun getAllOpportunitiesWithEagerRelationshipsIsEnabled() {
        val opportunityResource = OpportunityResource(opportunityServiceMock)
        `when`(opportunityServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restOpportunityMockMvc = MockMvcBuilders.standaloneSetup(opportunityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restOpportunityMockMvc.perform(get("/api/opportunities?eagerload=true"))
            .andExpect(status().isOk)

        verify(opportunityServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Suppress("unchecked")
    fun getAllOpportunitiesWithEagerRelationshipsIsNotEnabled() {
        val opportunityResource = OpportunityResource(opportunityServiceMock)
            `when`(opportunityServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))
        val restOpportunityMockMvc = MockMvcBuilders.standaloneSetup(opportunityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restOpportunityMockMvc.perform(get("/api/opportunities?eagerload=true"))
            .andExpect(status().isOk)

        verify(opportunityServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    fun getOpportunity() {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity)

        val id = opportunity.id
        assertNotNull(id)

        // Get the opportunity
        restOpportunityMockMvc.perform(get("/api/opportunities/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.opportunityTitle").value(DEFAULT_OPPORTUNITY_TITLE))
            .andExpect(jsonPath("$.opportunityDescription").value(DEFAULT_OPPORTUNITY_DESCRIPTION))
            .andExpect(jsonPath("$.weeklyTimeCommitment").value(DEFAULT_WEEKLY_TIME_COMMITMENT.toInt()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toInt()))
    }

    @Test
    @Transactional
    fun getNonExistingOpportunity() {
        // Get the opportunity
        restOpportunityMockMvc.perform(get("/api/opportunities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateOpportunity() {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity)

        val databaseSizeBeforeUpdate = opportunityRepository.findAll().size

        // Update the opportunity
        val id = opportunity.id
        assertNotNull(id)
        val updatedOpportunity = opportunityRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedOpportunity are not directly saved in db
        em.detach(updatedOpportunity)
        updatedOpportunity.opportunityTitle = UPDATED_OPPORTUNITY_TITLE
        updatedOpportunity.opportunityDescription = UPDATED_OPPORTUNITY_DESCRIPTION
        updatedOpportunity.weeklyTimeCommitment = UPDATED_WEEKLY_TIME_COMMITMENT
        updatedOpportunity.duration = UPDATED_DURATION
        val opportunityDTO = opportunityMapper.toDto(updatedOpportunity)

        restOpportunityMockMvc.perform(
            put("/api/opportunities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(opportunityDTO))
        ).andExpect(status().isOk)

        // Validate the Opportunity in the database
        val opportunityList = opportunityRepository.findAll()
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate)
        val testOpportunity = opportunityList[opportunityList.size - 1]
        assertThat(testOpportunity.opportunityTitle).isEqualTo(UPDATED_OPPORTUNITY_TITLE)
        assertThat(testOpportunity.opportunityDescription).isEqualTo(UPDATED_OPPORTUNITY_DESCRIPTION)
        assertThat(testOpportunity.weeklyTimeCommitment).isEqualTo(UPDATED_WEEKLY_TIME_COMMITMENT)
        assertThat(testOpportunity.duration).isEqualTo(UPDATED_DURATION)

        // Validate the Opportunity in Elasticsearch
        verify(mockOpportunitySearchRepository, times(1)).save(testOpportunity)
    }

    @Test
    @Transactional
    fun updateNonExistingOpportunity() {
        val databaseSizeBeforeUpdate = opportunityRepository.findAll().size

        // Create the Opportunity
        val opportunityDTO = opportunityMapper.toDto(opportunity)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpportunityMockMvc.perform(
            put("/api/opportunities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(opportunityDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Opportunity in the database
        val opportunityList = opportunityRepository.findAll()
        assertThat(opportunityList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Opportunity in Elasticsearch
        verify(mockOpportunitySearchRepository, times(0)).save(opportunity)
    }

    @Test
    @Transactional
    fun deleteOpportunity() {
        // Initialize the database
        opportunityRepository.saveAndFlush(opportunity)

        val databaseSizeBeforeDelete = opportunityRepository.findAll().size

        val id = opportunity.id
        assertNotNull(id)

        // Delete the opportunity
        restOpportunityMockMvc.perform(
            delete("/api/opportunities/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val opportunityList = opportunityRepository.findAll()
        assertThat(opportunityList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Opportunity in Elasticsearch
        verify(mockOpportunitySearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun searchOpportunity() {
        // InitializesearchOpportunity() the database
        opportunityRepository.saveAndFlush(opportunity)
        `when`(mockOpportunitySearchRepository.search(queryStringQuery("id:" + opportunity.id), PageRequest.of(0, 20)))
            .thenReturn(PageImpl(listOf(opportunity), PageRequest.of(0, 1), 1))
        // Search the opportunity
        restOpportunityMockMvc.perform(get("/api/_search/opportunities?query=id:" + opportunity.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opportunity.id?.toInt())))
            .andExpect(jsonPath("$.[*].opportunityTitle").value(hasItem(DEFAULT_OPPORTUNITY_TITLE)))
            .andExpect(jsonPath("$.[*].opportunityDescription").value(hasItem(DEFAULT_OPPORTUNITY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].weeklyTimeCommitment").value(hasItem(DEFAULT_WEEKLY_TIME_COMMITMENT.toInt())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toInt())))
    }

    companion object {

        private const val DEFAULT_OPPORTUNITY_TITLE = "AAAAAAAAAA"
        private const val UPDATED_OPPORTUNITY_TITLE = "BBBBBBBBBB"

        private const val DEFAULT_OPPORTUNITY_DESCRIPTION = "AAAAAAAAAA"
        private const val UPDATED_OPPORTUNITY_DESCRIPTION = "BBBBBBBBBB"

        private const val DEFAULT_WEEKLY_TIME_COMMITMENT: Long = 1L
        private const val UPDATED_WEEKLY_TIME_COMMITMENT: Long = 2L

        private const val DEFAULT_DURATION: Long = 1L
        private const val UPDATED_DURATION: Long = 2L

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Opportunity {
            val opportunity = Opportunity(
                opportunityTitle = DEFAULT_OPPORTUNITY_TITLE,
                opportunityDescription = DEFAULT_OPPORTUNITY_DESCRIPTION,
                weeklyTimeCommitment = DEFAULT_WEEKLY_TIME_COMMITMENT,
                duration = DEFAULT_DURATION
            )

            return opportunity
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Opportunity {
            val opportunity = Opportunity(
                opportunityTitle = UPDATED_OPPORTUNITY_TITLE,
                opportunityDescription = UPDATED_OPPORTUNITY_DESCRIPTION,
                weeklyTimeCommitment = UPDATED_WEEKLY_TIME_COMMITMENT,
                duration = UPDATED_DURATION
            )

            return opportunity
        }
    }
}
