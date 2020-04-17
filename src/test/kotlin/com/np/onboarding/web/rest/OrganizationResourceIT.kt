package com.np.onboarding.web.rest

import com.np.onboarding.ResourceroutingApp
import com.np.onboarding.domain.Organization
import com.np.onboarding.repository.OrganizationRepository
import com.np.onboarding.repository.search.OrganizationSearchRepository
import com.np.onboarding.service.OrganizationService
import com.np.onboarding.service.mapper.OrganizationMapper
import com.np.onboarding.web.rest.errors.ExceptionTranslator
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
 * Integration tests for the [OrganizationResource] REST controller.
 *
 * @see OrganizationResource
 */
@SpringBootTest(classes = [ResourceroutingApp::class])
class OrganizationResourceIT {

    @Autowired
    private lateinit var organizationRepository: OrganizationRepository

    @Autowired
    private lateinit var organizationMapper: OrganizationMapper

    @Autowired
    private lateinit var organizationService: OrganizationService

    /**
     * This repository is mocked in the com.np.onboarding.repository.search test package.
     *
     * @see com.np.onboarding.repository.search.OrganizationSearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockOrganizationSearchRepository: OrganizationSearchRepository

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

    private lateinit var restOrganizationMockMvc: MockMvc

    private lateinit var organization: Organization

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val organizationResource = OrganizationResource(organizationService)
        this.restOrganizationMockMvc = MockMvcBuilders.standaloneSetup(organizationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        organization = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createOrganization() {
        val databaseSizeBeforeCreate = organizationRepository.findAll().size

        // Create the Organization
        val organizationDTO = organizationMapper.toDto(organization)
        restOrganizationMockMvc.perform(
            post("/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(organizationDTO))
        ).andExpect(status().isCreated)

        // Validate the Organization in the database
        val organizationList = organizationRepository.findAll()
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate + 1)
        val testOrganization = organizationList[organizationList.size - 1]
        assertThat(testOrganization.organizationName).isEqualTo(DEFAULT_ORGANIZATION_NAME)

        // Validate the Organization in Elasticsearch
    }

    @Test
    @Transactional
    fun createOrganizationWithExistingId() {
        val databaseSizeBeforeCreate = organizationRepository.findAll().size

        // Create the Organization with an existing ID
        organization.id = 1L
        val organizationDTO = organizationMapper.toDto(organization)

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMockMvc.perform(
            post("/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(organizationDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Organization in the database
        val organizationList = organizationRepository.findAll()
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate)

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(0)).save(organization)
    }

    @Test
    @Transactional
    fun checkOrganizationNameIsRequired() {
        val databaseSizeBeforeTest = organizationRepository.findAll().size
        // set the field null
        organization.organizationName = null

        // Create the Organization, which fails.
        val organizationDTO = organizationMapper.toDto(organization)

        restOrganizationMockMvc.perform(
            post("/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(organizationDTO))
        ).andExpect(status().isBadRequest)

        val organizationList = organizationRepository.findAll()
        assertThat(organizationList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllOrganizations() {
        // Initialize the database
        organizationRepository.saveAndFlush(organization)

        // Get all the organizationList
        restOrganizationMockMvc.perform(get("/api/organizations?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.id?.toInt())))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
    }

    @Test
    @Transactional
    fun getOrganization() {
        // Initialize the database
        organizationRepository.saveAndFlush(organization)

        val id = organization.id
        assertNotNull(id)

        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.organizationName").value(DEFAULT_ORGANIZATION_NAME))
    }

    @Test
    @Transactional
    fun getNonExistingOrganization() {
        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateOrganization() {
        // Initialize the database
        organizationRepository.saveAndFlush(organization)

        val databaseSizeBeforeUpdate = organizationRepository.findAll().size

        // Update the organization
        val id = organization.id
        assertNotNull(id)
        val updatedOrganization = organizationRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedOrganization are not directly saved in db
        em.detach(updatedOrganization)
        updatedOrganization.organizationName = UPDATED_ORGANIZATION_NAME
        val organizationDTO = organizationMapper.toDto(updatedOrganization)

        restOrganizationMockMvc.perform(
            put("/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(organizationDTO))
        ).andExpect(status().isOk)

        // Validate the Organization in the database
        val organizationList = organizationRepository.findAll()
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate)
        val testOrganization = organizationList[organizationList.size - 1]
        assertThat(testOrganization.organizationName).isEqualTo(UPDATED_ORGANIZATION_NAME)

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(1)).save(testOrganization)
    }

    @Test
    @Transactional
    fun updateNonExistingOrganization() {
        val databaseSizeBeforeUpdate = organizationRepository.findAll().size

        // Create the Organization
        val organizationDTO = organizationMapper.toDto(organization)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc.perform(
            put("/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(organizationDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Organization in the database
        val organizationList = organizationRepository.findAll()
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(0)).save(organization)
    }

    @Test
    @Transactional
    fun deleteOrganization() {
        // Initialize the database
        organizationRepository.saveAndFlush(organization)

        val databaseSizeBeforeDelete = organizationRepository.findAll().size

        val id = organization.id
        assertNotNull(id)

        // Delete the organization
        restOrganizationMockMvc.perform(
            delete("/api/organizations/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val organizationList = organizationRepository.findAll()
        assertThat(organizationList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Organization in Elasticsearch
        verify(mockOrganizationSearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun searchOrganization() {
        // InitializesearchOrganization() the database
        organizationRepository.saveAndFlush(organization)
        `when`(mockOrganizationSearchRepository.search(queryStringQuery("id:" + organization.id)))
            .thenReturn(listOf(organization))
        // Search the organization
        restOrganizationMockMvc.perform(get("/api/_search/organizations?query=id:" + organization.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.id?.toInt())))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
    }

    companion object {

        private const val DEFAULT_ORGANIZATION_NAME = "AAAAAAAAAA"
        private const val UPDATED_ORGANIZATION_NAME = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Organization {
            val organization = Organization(
                organizationName = DEFAULT_ORGANIZATION_NAME
            )

            return organization
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Organization {
            val organization = Organization(
                organizationName = UPDATED_ORGANIZATION_NAME
            )

            return organization
        }
    }
}
