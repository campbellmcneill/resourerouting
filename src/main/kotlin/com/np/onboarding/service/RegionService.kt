package com.np.onboarding.service
import com.np.onboarding.service.dto.RegionDTO
import java.util.Optional

/**
 * Service Interface for managing [com.np.onboarding.domain.Region].
 */
interface RegionService {

    /**
     * Save a region.
     *
     * @param regionDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(regionDTO: RegionDTO): RegionDTO

    /**
     * Get all the regions.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<RegionDTO>

    /**
     * Get the "id" region.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<RegionDTO>

    /**
     * Delete the "id" region.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)

    /**
     * Search for the region corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    fun search(query: String): MutableList<RegionDTO>
}
