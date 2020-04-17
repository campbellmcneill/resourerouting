package com.np.onboarding.web.rest

import com.np.onboarding.ResourceroutingApp
import com.np.onboarding.domain.Volunteer
import com.np.onboarding.repository.VolunteerRepository
import com.np.onboarding.repository.search.VolunteerSearchRepository
import com.np.onboarding.service.VolunteerService
import com.np.onboarding.service.mapper.VolunteerMapper
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
 * Integration tests for the [VolunteerResource] REST controller.
 *
 * @see VolunteerResource
 */
@SpringBootTest(classes = [ResourceroutingApp::class])
class VolunteerResourceIT {

    @Autowired
    private lateinit var volunteerRepository: VolunteerRepository

    @Autowired
    private lateinit var volunteerMapper: VolunteerMapper

    @Autowired
    private lateinit var volunteerService: VolunteerService

    /**
     * This repository is mocked in the com.np.onboarding.repository.search test package.
     *
     * @see com.np.onboarding.repository.search.VolunteerSearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockVolunteerSearchRepository: VolunteerSearchRepository

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

    private lateinit var restVolunteerMockMvc: MockMvc

    private lateinit var volunteer: Volunteer

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val volunteerResource = VolunteerResource(volunteerService)
        this.restVolunteerMockMvc = MockMvcBuilders.standaloneSetup(volunteerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        volunteer = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createVolunteer() {
        val databaseSizeBeforeCreate = volunteerRepository.findAll().size

        // Create the Volunteer
        val volunteerDTO = volunteerMapper.toDto(volunteer)
        restVolunteerMockMvc.perform(
            post("/api/volunteers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(volunteerDTO))
        ).andExpect(status().isCreated)

        // Validate the Volunteer in the database
        val volunteerList = volunteerRepository.findAll()
        assertThat(volunteerList).hasSize(databaseSizeBeforeCreate + 1)
        val testVolunteer = volunteerList[volunteerList.size - 1]
        assertThat(testVolunteer.firstName).isEqualTo(DEFAULT_FIRST_NAME)
        assertThat(testVolunteer.lastName).isEqualTo(DEFAULT_LAST_NAME)
        assertThat(testVolunteer.email).isEqualTo(DEFAULT_EMAIL)
        assertThat(testVolunteer.phoneNumber).isEqualTo(DEFAULT_PHONE_NUMBER)
        assertThat(testVolunteer.hireDate).isEqualTo(DEFAULT_HIRE_DATE)

        // Validate the Volunteer in Elasticsearch
    }

    @Test
    @Transactional
    fun createVolunteerWithExistingId() {
        val databaseSizeBeforeCreate = volunteerRepository.findAll().size

        // Create the Volunteer with an existing ID
        volunteer.id = 1L
        val volunteerDTO = volunteerMapper.toDto(volunteer)

        // An entity with an existing ID cannot be created, so this API call must fail
        restVolunteerMockMvc.perform(
            post("/api/volunteers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(volunteerDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Volunteer in the database
        val volunteerList = volunteerRepository.findAll()
        assertThat(volunteerList).hasSize(databaseSizeBeforeCreate)

        // Validate the Volunteer in Elasticsearch
        verify(mockVolunteerSearchRepository, times(0)).save(volunteer)
    }

    @Test
    @Transactional
    fun getAllVolunteers() {
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer)

        // Get all the volunteerList
        restVolunteerMockMvc.perform(get("/api/volunteers?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(volunteer.id?.toInt())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
    }

    @Test
    @Transactional
    fun getVolunteer() {
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer)

        val id = volunteer.id
        assertNotNull(id)

        // Get the volunteer
        restVolunteerMockMvc.perform(get("/api/volunteers/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
    }

    @Test
    @Transactional
    fun getNonExistingVolunteer() {
        // Get the volunteer
        restVolunteerMockMvc.perform(get("/api/volunteers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateVolunteer() {
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer)

        val databaseSizeBeforeUpdate = volunteerRepository.findAll().size

        // Update the volunteer
        val id = volunteer.id
        assertNotNull(id)
        val updatedVolunteer = volunteerRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedVolunteer are not directly saved in db
        em.detach(updatedVolunteer)
        updatedVolunteer.firstName = UPDATED_FIRST_NAME
        updatedVolunteer.lastName = UPDATED_LAST_NAME
        updatedVolunteer.email = UPDATED_EMAIL
        updatedVolunteer.phoneNumber = UPDATED_PHONE_NUMBER
        updatedVolunteer.hireDate = UPDATED_HIRE_DATE
        val volunteerDTO = volunteerMapper.toDto(updatedVolunteer)

        restVolunteerMockMvc.perform(
            put("/api/volunteers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(volunteerDTO))
        ).andExpect(status().isOk)

        // Validate the Volunteer in the database
        val volunteerList = volunteerRepository.findAll()
        assertThat(volunteerList).hasSize(databaseSizeBeforeUpdate)
        val testVolunteer = volunteerList[volunteerList.size - 1]
        assertThat(testVolunteer.firstName).isEqualTo(UPDATED_FIRST_NAME)
        assertThat(testVolunteer.lastName).isEqualTo(UPDATED_LAST_NAME)
        assertThat(testVolunteer.email).isEqualTo(UPDATED_EMAIL)
        assertThat(testVolunteer.phoneNumber).isEqualTo(UPDATED_PHONE_NUMBER)
        assertThat(testVolunteer.hireDate).isEqualTo(UPDATED_HIRE_DATE)

        // Validate the Volunteer in Elasticsearch
        verify(mockVolunteerSearchRepository, times(1)).save(testVolunteer)
    }

    @Test
    @Transactional
    fun updateNonExistingVolunteer() {
        val databaseSizeBeforeUpdate = volunteerRepository.findAll().size

        // Create the Volunteer
        val volunteerDTO = volunteerMapper.toDto(volunteer)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVolunteerMockMvc.perform(
            put("/api/volunteers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(volunteerDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Volunteer in the database
        val volunteerList = volunteerRepository.findAll()
        assertThat(volunteerList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Volunteer in Elasticsearch
        verify(mockVolunteerSearchRepository, times(0)).save(volunteer)
    }

    @Test
    @Transactional
    fun deleteVolunteer() {
        // Initialize the database
        volunteerRepository.saveAndFlush(volunteer)

        val databaseSizeBeforeDelete = volunteerRepository.findAll().size

        val id = volunteer.id
        assertNotNull(id)

        // Delete the volunteer
        restVolunteerMockMvc.perform(
            delete("/api/volunteers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val volunteerList = volunteerRepository.findAll()
        assertThat(volunteerList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Volunteer in Elasticsearch
        verify(mockVolunteerSearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun searchVolunteer() {
        // InitializesearchVolunteer() the database
        volunteerRepository.saveAndFlush(volunteer)
        `when`(mockVolunteerSearchRepository.search(queryStringQuery("id:" + volunteer.id), PageRequest.of(0, 20)))
            .thenReturn(PageImpl(listOf(volunteer), PageRequest.of(0, 1), 1))
        // Search the volunteer
        restVolunteerMockMvc.perform(get("/api/_search/volunteers?query=id:" + volunteer.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(volunteer.id?.toInt())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
    }

    companion object {

        private const val DEFAULT_FIRST_NAME = "AAAAAAAAAA"
        private const val UPDATED_FIRST_NAME = "BBBBBBBBBB"

        private const val DEFAULT_LAST_NAME = "AAAAAAAAAA"
        private const val UPDATED_LAST_NAME = "BBBBBBBBBB"

        private const val DEFAULT_EMAIL = "AAAAAAAAAA"
        private const val UPDATED_EMAIL = "BBBBBBBBBB"

        private const val DEFAULT_PHONE_NUMBER = "AAAAAAAAAA"
        private const val UPDATED_PHONE_NUMBER = "BBBBBBBBBB"

        private val DEFAULT_HIRE_DATE: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_HIRE_DATE: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Volunteer {
            val volunteer = Volunteer(
                firstName = DEFAULT_FIRST_NAME,
                lastName = DEFAULT_LAST_NAME,
                email = DEFAULT_EMAIL,
                phoneNumber = DEFAULT_PHONE_NUMBER,
                hireDate = DEFAULT_HIRE_DATE
            )

            return volunteer
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Volunteer {
            val volunteer = Volunteer(
                firstName = UPDATED_FIRST_NAME,
                lastName = UPDATED_LAST_NAME,
                email = UPDATED_EMAIL,
                phoneNumber = UPDATED_PHONE_NUMBER,
                hireDate = UPDATED_HIRE_DATE
            )

            return volunteer
        }
    }
}
