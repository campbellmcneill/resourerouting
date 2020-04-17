package com.np.onboarding.web.rest

import com.np.onboarding.ResourceroutingApp
import com.np.onboarding.domain.OpportunityHistory
import com.np.onboarding.domain.enumeration.Language
import com.np.onboarding.repository.OpportunityHistoryRepository
import com.np.onboarding.repository.search.OpportunityHistorySearchRepository
import com.np.onboarding.service.OpportunityHistoryService
import com.np.onboarding.service.mapper.OpportunityHistoryMapper
import com.np.onboarding.web.rest.errors.ExceptionTranslator
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.persistence.EntityManager
import kotlin.test.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
 * Integration tests for the [OpportunityHistoryResource] REST controller.
 *
 * @see OpportunityHistoryResource
 */
@SpringBootTest(classes = [ResourceroutingApp::class])
class OpportunityHistoryResourceIT {

    @Autowired
    private lateinit var opportunityHistoryRepository: OpportunityHistoryRepository

    @Autowired
    private lateinit var opportunityHistoryMapper: OpportunityHistoryMapper

    @Autowired
    private lateinit var opportunityHistoryService: OpportunityHistoryService

    /**
     * This repository is mocked in the com.np.onboarding.repository.search test package.
     *
     * @see com.np.onboarding.repository.search.OpportunityHistorySearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockOpportunityHistorySearchRepository: OpportunityHistorySearchRepository

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

    private lateinit var restOpportunityHistoryMockMvc: MockMvc

    private lateinit var opportunityHistory: OpportunityHistory

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val opportunityHistoryResource = OpportunityHistoryResource(opportunityHistoryService)
        this.restOpportunityHistoryMockMvc = MockMvcBuilders.standaloneSetup(opportunityHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        opportunityHistory = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createOpportunityHistory() {
        val databaseSizeBeforeCreate = opportunityHistoryRepository.findAll().size

        // Create the OpportunityHistory
        val opportunityHistoryDTO = opportunityHistoryMapper.toDto(opportunityHistory)
        restOpportunityHistoryMockMvc.perform(
            post("/api/opportunity-histories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(opportunityHistoryDTO))
        ).andExpect(status().isCreated)

        // Validate the OpportunityHistory in the database
        val opportunityHistoryList = opportunityHistoryRepository.findAll()
        assertThat(opportunityHistoryList).hasSize(databaseSizeBeforeCreate + 1)
        val testOpportunityHistory = opportunityHistoryList[opportunityHistoryList.size - 1]
        assertThat(testOpportunityHistory.startDate).isEqualTo(DEFAULT_START_DATE)
        assertThat(testOpportunityHistory.endDate).isEqualTo(DEFAULT_END_DATE)
        assertThat(testOpportunityHistory.rating).isEqualTo(DEFAULT_RATING)
        assertThat(testOpportunityHistory.comments).isEqualTo(DEFAULT_COMMENTS)
        assertThat(testOpportunityHistory.language).isEqualTo(DEFAULT_LANGUAGE)

        // Validate the OpportunityHistory in Elasticsearch
    }

    @Test
    @Transactional
    fun createOpportunityHistoryWithExistingId() {
        val databaseSizeBeforeCreate = opportunityHistoryRepository.findAll().size

        // Create the OpportunityHistory with an existing ID
        opportunityHistory.id = 1L
        val opportunityHistoryDTO = opportunityHistoryMapper.toDto(opportunityHistory)

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpportunityHistoryMockMvc.perform(
            post("/api/opportunity-histories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(opportunityHistoryDTO))
        ).andExpect(status().isBadRequest)

        // Validate the OpportunityHistory in the database
        val opportunityHistoryList = opportunityHistoryRepository.findAll()
        assertThat(opportunityHistoryList).hasSize(databaseSizeBeforeCreate)

        // Validate the OpportunityHistory in Elasticsearch
        verify(mockOpportunityHistorySearchRepository, times(0)).save(opportunityHistory)
    }

    @Test
    @Transactional
    fun getAllOpportunityHistories() {
        // Initialize the database
        opportunityHistoryRepository.saveAndFlush(opportunityHistory)

        // Get all the opportunityHistoryList
        restOpportunityHistoryMockMvc.perform(get("/api/opportunity-histories?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opportunityHistory.id?.toInt())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.toInt())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
    }

    @Test
    @Transactional
    fun getOpportunityHistory() {
        // Initialize the database
        opportunityHistoryRepository.saveAndFlush(opportunityHistory)

        val id = opportunityHistory.id
        assertNotNull(id)

        // Get the opportunityHistory
        restOpportunityHistoryMockMvc.perform(get("/api/opportunity-histories/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.toInt()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
    }

    @Test
    @Transactional
    fun getNonExistingOpportunityHistory() {
        // Get the opportunityHistory
        restOpportunityHistoryMockMvc.perform(get("/api/opportunity-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateOpportunityHistory() {
        // Initialize the database
        opportunityHistoryRepository.saveAndFlush(opportunityHistory)

        val databaseSizeBeforeUpdate = opportunityHistoryRepository.findAll().size

        // Update the opportunityHistory
        val id = opportunityHistory.id
        assertNotNull(id)
        val updatedOpportunityHistory = opportunityHistoryRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedOpportunityHistory are not directly saved in db
        em.detach(updatedOpportunityHistory)
        updatedOpportunityHistory.startDate = UPDATED_START_DATE
        updatedOpportunityHistory.endDate = UPDATED_END_DATE
        updatedOpportunityHistory.rating = UPDATED_RATING
        updatedOpportunityHistory.comments = UPDATED_COMMENTS
        updatedOpportunityHistory.language = UPDATED_LANGUAGE
        val opportunityHistoryDTO = opportunityHistoryMapper.toDto(updatedOpportunityHistory)

        restOpportunityHistoryMockMvc.perform(
            put("/api/opportunity-histories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(opportunityHistoryDTO))
        ).andExpect(status().isOk)

        // Validate the OpportunityHistory in the database
        val opportunityHistoryList = opportunityHistoryRepository.findAll()
        assertThat(opportunityHistoryList).hasSize(databaseSizeBeforeUpdate)
        val testOpportunityHistory = opportunityHistoryList[opportunityHistoryList.size - 1]
        assertThat(testOpportunityHistory.startDate).isEqualTo(UPDATED_START_DATE)
        assertThat(testOpportunityHistory.endDate).isEqualTo(UPDATED_END_DATE)
        assertThat(testOpportunityHistory.rating).isEqualTo(UPDATED_RATING)
        assertThat(testOpportunityHistory.comments).isEqualTo(UPDATED_COMMENTS)
        assertThat(testOpportunityHistory.language).isEqualTo(UPDATED_LANGUAGE)

        // Validate the OpportunityHistory in Elasticsearch
        verify(mockOpportunityHistorySearchRepository, times(1)).save(testOpportunityHistory)
    }

    @Test
    @Transactional
    fun updateNonExistingOpportunityHistory() {
        val databaseSizeBeforeUpdate = opportunityHistoryRepository.findAll().size

        // Create the OpportunityHistory
        val opportunityHistoryDTO = opportunityHistoryMapper.toDto(opportunityHistory)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpportunityHistoryMockMvc.perform(
            put("/api/opportunity-histories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(opportunityHistoryDTO))
        ).andExpect(status().isBadRequest)

        // Validate the OpportunityHistory in the database
        val opportunityHistoryList = opportunityHistoryRepository.findAll()
        assertThat(opportunityHistoryList).hasSize(databaseSizeBeforeUpdate)

        // Validate the OpportunityHistory in Elasticsearch
        verify(mockOpportunityHistorySearchRepository, times(0)).save(opportunityHistory)
    }

    @Test
    @Transactional
    fun deleteOpportunityHistory() {
        // Initialize the database
        opportunityHistoryRepository.saveAndFlush(opportunityHistory)

        val databaseSizeBeforeDelete = opportunityHistoryRepository.findAll().size

        val id = opportunityHistory.id
        assertNotNull(id)

        // Delete the opportunityHistory
        restOpportunityHistoryMockMvc.perform(
            delete("/api/opportunity-histories/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val opportunityHistoryList = opportunityHistoryRepository.findAll()
        assertThat(opportunityHistoryList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the OpportunityHistory in Elasticsearch
        verify(mockOpportunityHistorySearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun searchOpportunityHistory() {
        // InitializesearchOpportunityHistory() the database
        opportunityHistoryRepository.saveAndFlush(opportunityHistory)
        `when`(mockOpportunityHistorySearchRepository.search(queryStringQuery("id:" + opportunityHistory.id), PageRequest.of(0, 20)))
            .thenReturn(PageImpl(listOf(opportunityHistory), PageRequest.of(0, 1), 1))
        // Search the opportunityHistory
        restOpportunityHistoryMockMvc.perform(get("/api/_search/opportunity-histories?query=id:" + opportunityHistory.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opportunityHistory.id?.toInt())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.toInt())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
    }

    companion object {

        private val DEFAULT_START_DATE: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_START_DATE: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private val DEFAULT_END_DATE: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_END_DATE: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private const val DEFAULT_RATING: Long = 1L
        private const val UPDATED_RATING: Long = 2L

        private const val DEFAULT_COMMENTS = "AAAAAAAAAA"
        private const val UPDATED_COMMENTS = "BBBBBBBBBB"

        private val DEFAULT_LANGUAGE: Language = Language.FRENCH
        private val UPDATED_LANGUAGE: Language = Language.ENGLISH

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): OpportunityHistory {
            val opportunityHistory = OpportunityHistory(
                startDate = DEFAULT_START_DATE,
                endDate = DEFAULT_END_DATE,
                rating = DEFAULT_RATING,
                comments = DEFAULT_COMMENTS,
                language = DEFAULT_LANGUAGE
            )

            return opportunityHistory
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): OpportunityHistory {
            val opportunityHistory = OpportunityHistory(
                startDate = UPDATED_START_DATE,
                endDate = UPDATED_END_DATE,
                rating = UPDATED_RATING,
                comments = UPDATED_COMMENTS,
                language = UPDATED_LANGUAGE
            )

            return opportunityHistory
        }
    }
}
