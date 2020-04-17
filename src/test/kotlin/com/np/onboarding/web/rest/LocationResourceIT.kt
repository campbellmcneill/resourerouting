package com.np.onboarding.web.rest

import com.np.onboarding.ResourceroutingApp
import com.np.onboarding.domain.Location
import com.np.onboarding.repository.LocationRepository
import com.np.onboarding.repository.search.LocationSearchRepository
import com.np.onboarding.service.LocationService
import com.np.onboarding.service.mapper.LocationMapper
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
 * Integration tests for the [LocationResource] REST controller.
 *
 * @see LocationResource
 */
@SpringBootTest(classes = [ResourceroutingApp::class])
class LocationResourceIT {

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var locationMapper: LocationMapper

    @Autowired
    private lateinit var locationService: LocationService

    /**
     * This repository is mocked in the com.np.onboarding.repository.search test package.
     *
     * @see com.np.onboarding.repository.search.LocationSearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockLocationSearchRepository: LocationSearchRepository

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

    private lateinit var restLocationMockMvc: MockMvc

    private lateinit var location: Location

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val locationResource = LocationResource(locationService)
        this.restLocationMockMvc = MockMvcBuilders.standaloneSetup(locationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        location = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createLocation() {
        val databaseSizeBeforeCreate = locationRepository.findAll().size

        // Create the Location
        val locationDTO = locationMapper.toDto(location)
        restLocationMockMvc.perform(
            post("/api/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(locationDTO))
        ).andExpect(status().isCreated)

        // Validate the Location in the database
        val locationList = locationRepository.findAll()
        assertThat(locationList).hasSize(databaseSizeBeforeCreate + 1)
        val testLocation = locationList[locationList.size - 1]
        assertThat(testLocation.streetAddress).isEqualTo(DEFAULT_STREET_ADDRESS)
        assertThat(testLocation.postalCode).isEqualTo(DEFAULT_POSTAL_CODE)
        assertThat(testLocation.city).isEqualTo(DEFAULT_CITY)
        assertThat(testLocation.stateProvince).isEqualTo(DEFAULT_STATE_PROVINCE)

        // Validate the Location in Elasticsearch
    }

    @Test
    @Transactional
    fun createLocationWithExistingId() {
        val databaseSizeBeforeCreate = locationRepository.findAll().size

        // Create the Location with an existing ID
        location.id = 1L
        val locationDTO = locationMapper.toDto(location)

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc.perform(
            post("/api/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(locationDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Location in the database
        val locationList = locationRepository.findAll()
        assertThat(locationList).hasSize(databaseSizeBeforeCreate)

        // Validate the Location in Elasticsearch
        verify(mockLocationSearchRepository, times(0)).save(location)
    }

    @Test
    @Transactional
    fun getAllLocations() {
        // Initialize the database
        locationRepository.saveAndFlush(location)

        // Get all the locationList
        restLocationMockMvc.perform(get("/api/locations?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.id?.toInt())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)))
    }

    @Test
    @Transactional
    fun getLocation() {
        // Initialize the database
        locationRepository.saveAndFlush(location)

        val id = location.id
        assertNotNull(id)

        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE))
    }

    @Test
    @Transactional
    fun getNonExistingLocation() {
        // Get the location
        restLocationMockMvc.perform(get("/api/locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateLocation() {
        // Initialize the database
        locationRepository.saveAndFlush(location)

        val databaseSizeBeforeUpdate = locationRepository.findAll().size

        // Update the location
        val id = location.id
        assertNotNull(id)
        val updatedLocation = locationRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
        em.detach(updatedLocation)
        updatedLocation.streetAddress = UPDATED_STREET_ADDRESS
        updatedLocation.postalCode = UPDATED_POSTAL_CODE
        updatedLocation.city = UPDATED_CITY
        updatedLocation.stateProvince = UPDATED_STATE_PROVINCE
        val locationDTO = locationMapper.toDto(updatedLocation)

        restLocationMockMvc.perform(
            put("/api/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(locationDTO))
        ).andExpect(status().isOk)

        // Validate the Location in the database
        val locationList = locationRepository.findAll()
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate)
        val testLocation = locationList[locationList.size - 1]
        assertThat(testLocation.streetAddress).isEqualTo(UPDATED_STREET_ADDRESS)
        assertThat(testLocation.postalCode).isEqualTo(UPDATED_POSTAL_CODE)
        assertThat(testLocation.city).isEqualTo(UPDATED_CITY)
        assertThat(testLocation.stateProvince).isEqualTo(UPDATED_STATE_PROVINCE)

        // Validate the Location in Elasticsearch
        verify(mockLocationSearchRepository, times(1)).save(testLocation)
    }

    @Test
    @Transactional
    fun updateNonExistingLocation() {
        val databaseSizeBeforeUpdate = locationRepository.findAll().size

        // Create the Location
        val locationDTO = locationMapper.toDto(location)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc.perform(
            put("/api/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(locationDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Location in the database
        val locationList = locationRepository.findAll()
        assertThat(locationList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Location in Elasticsearch
        verify(mockLocationSearchRepository, times(0)).save(location)
    }

    @Test
    @Transactional
    fun deleteLocation() {
        // Initialize the database
        locationRepository.saveAndFlush(location)

        val databaseSizeBeforeDelete = locationRepository.findAll().size

        val id = location.id
        assertNotNull(id)

        // Delete the location
        restLocationMockMvc.perform(
            delete("/api/locations/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val locationList = locationRepository.findAll()
        assertThat(locationList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Location in Elasticsearch
        verify(mockLocationSearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun searchLocation() {
        // InitializesearchLocation() the database
        locationRepository.saveAndFlush(location)
        `when`(mockLocationSearchRepository.search(queryStringQuery("id:" + location.id)))
            .thenReturn(listOf(location))
        // Search the location
        restLocationMockMvc.perform(get("/api/_search/locations?query=id:" + location.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.id?.toInt())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)))
    }

    companion object {

        private const val DEFAULT_STREET_ADDRESS = "AAAAAAAAAA"
        private const val UPDATED_STREET_ADDRESS = "BBBBBBBBBB"

        private const val DEFAULT_POSTAL_CODE = "AAAAAAAAAA"
        private const val UPDATED_POSTAL_CODE = "BBBBBBBBBB"

        private const val DEFAULT_CITY = "AAAAAAAAAA"
        private const val UPDATED_CITY = "BBBBBBBBBB"

        private const val DEFAULT_STATE_PROVINCE = "AAAAAAAAAA"
        private const val UPDATED_STATE_PROVINCE = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Location {
            val location = Location(
                streetAddress = DEFAULT_STREET_ADDRESS,
                postalCode = DEFAULT_POSTAL_CODE,
                city = DEFAULT_CITY,
                stateProvince = DEFAULT_STATE_PROVINCE
            )

            return location
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Location {
            val location = Location(
                streetAddress = UPDATED_STREET_ADDRESS,
                postalCode = UPDATED_POSTAL_CODE,
                city = UPDATED_CITY,
                stateProvince = UPDATED_STATE_PROVINCE
            )

            return location
        }
    }
}
