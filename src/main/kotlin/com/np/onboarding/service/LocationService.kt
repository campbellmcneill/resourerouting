package com.np.onboarding.service
import com.np.onboarding.service.dto.LocationDTO
import java.util.Optional

/**
 * Service Interface for managing [com.np.onboarding.domain.Location].
 */
interface LocationService {

    /**
     * Save a location.
     *
     * @param locationDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(locationDTO: LocationDTO): LocationDTO

    /**
     * Get all the locations.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<LocationDTO>

    /**
     * Get the "id" location.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<LocationDTO>

    /**
     * Delete the "id" location.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)

    /**
     * Search for the location corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    fun search(query: String): MutableList<LocationDTO>
}
