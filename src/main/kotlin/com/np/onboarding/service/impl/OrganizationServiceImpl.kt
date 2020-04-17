package com.np.onboarding.service.impl

import com.np.onboarding.domain.Organization
import com.np.onboarding.repository.OrganizationRepository
import com.np.onboarding.repository.search.OrganizationSearchRepository
import com.np.onboarding.service.OrganizationService
import com.np.onboarding.service.dto.OrganizationDTO
import com.np.onboarding.service.mapper.OrganizationMapper
import java.util.Optional
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service Implementation for managing [Organization].
 */
@Service
@Transactional
class OrganizationServiceImpl(
    private val organizationRepository: OrganizationRepository,
    private val organizationMapper: OrganizationMapper,
    private val organizationSearchRepository: OrganizationSearchRepository
) : OrganizationService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(organizationDTO: OrganizationDTO): OrganizationDTO {
        log.debug("Request to save Organization : {}", organizationDTO)

        var organization = organizationMapper.toEntity(organizationDTO)
        organization = organizationRepository.save(organization)
        val result = organizationMapper.toDto(organization)
        organizationSearchRepository.save(organization)
        return result
    }

    /**
     * Get all the organizations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(): MutableList<OrganizationDTO> {
        log.debug("Request to get all Organizations")
        return organizationRepository.findAll()
            .mapTo(mutableListOf(), organizationMapper::toDto)
    }

    /**
     * Get one organization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<OrganizationDTO> {
        log.debug("Request to get Organization : {}", id)
        return organizationRepository.findById(id)
            .map(organizationMapper::toDto)
    }

    /**
     * Delete the organization by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Organization : {}", id)

        organizationRepository.deleteById(id)
        organizationSearchRepository.deleteById(id)
    }

    /**
     * Search for the organization corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun search(query: String): MutableList<OrganizationDTO> {
        log.debug("Request to search Organizations for query {}", query)
        return organizationSearchRepository.search(queryStringQuery(query))
            .mapTo(mutableListOf(), organizationMapper::toDto)
    }
}
