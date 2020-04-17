package com.np.onboarding.service
import com.np.onboarding.service.dto.OrganizationDTO
import java.util.Optional

/**
 * Service Interface for managing [com.np.onboarding.domain.Organization].
 */
interface OrganizationService {

    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(organizationDTO: OrganizationDTO): OrganizationDTO

    /**
     * Get all the organizations.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<OrganizationDTO>

    /**
     * Get the "id" organization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<OrganizationDTO>

    /**
     * Delete the "id" organization.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)

    /**
     * Search for the organization corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    fun search(query: String): MutableList<OrganizationDTO>
}
