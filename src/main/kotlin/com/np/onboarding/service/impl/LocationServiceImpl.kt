package com.np.onboarding.service.impl

import com.np.onboarding.domain.Location
import com.np.onboarding.repository.LocationRepository
import com.np.onboarding.repository.search.LocationSearchRepository
import com.np.onboarding.service.LocationService
import com.np.onboarding.service.dto.LocationDTO
import com.np.onboarding.service.mapper.LocationMapper
import java.util.Optional
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service Implementation for managing [Location].
 */
@Service
@Transactional
class LocationServiceImpl(
    private val locationRepository: LocationRepository,
    private val locationMapper: LocationMapper,
    private val locationSearchRepository: LocationSearchRepository
) : LocationService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a location.
     *
     * @param locationDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(locationDTO: LocationDTO): LocationDTO {
        log.debug("Request to save Location : {}", locationDTO)

        var location = locationMapper.toEntity(locationDTO)
        location = locationRepository.save(location)
        val result = locationMapper.toDto(location)
        locationSearchRepository.save(location)
        return result
    }

    /**
     * Get all the locations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(): MutableList<LocationDTO> {
        log.debug("Request to get all Locations")
        return locationRepository.findAll()
            .mapTo(mutableListOf(), locationMapper::toDto)
    }

    /**
     * Get one location by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<LocationDTO> {
        log.debug("Request to get Location : {}", id)
        return locationRepository.findById(id)
            .map(locationMapper::toDto)
    }

    /**
     * Delete the location by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Location : {}", id)

        locationRepository.deleteById(id)
        locationSearchRepository.deleteById(id)
    }

    /**
     * Search for the location corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun search(query: String): MutableList<LocationDTO> {
        log.debug("Request to search Locations for query {}", query)
        return locationSearchRepository.search(queryStringQuery(query))
            .mapTo(mutableListOf(), locationMapper::toDto)
    }
}
