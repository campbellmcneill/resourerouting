package com.np.onboarding.service
import com.np.onboarding.domain.Opportunity
import com.np.onboarding.repository.OpportunityRepository
import com.np.onboarding.repository.search.OpportunitySearchRepository
import com.np.onboarding.service.dto.OpportunityDTO
import com.np.onboarding.service.mapper.OpportunityMapper
import java.util.Optional
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service Implementation for managing [Opportunity].
 */
@Service
@Transactional
class OpportunityService(
    private val opportunityRepository: OpportunityRepository,
    private val opportunityMapper: OpportunityMapper,
    private val opportunitySearchRepository: OpportunitySearchRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a opportunity.
     *
     * @param opportunityDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(opportunityDTO: OpportunityDTO): OpportunityDTO {
        log.debug("Request to save Opportunity : {}", opportunityDTO)

        var opportunity = opportunityMapper.toEntity(opportunityDTO)
        opportunity = opportunityRepository.save(opportunity)
        val result = opportunityMapper.toDto(opportunity)
        opportunitySearchRepository.save(opportunity)
        return result
    }

    /**
     * Get all the opportunities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<OpportunityDTO> {
        log.debug("Request to get all Opportunities")
        return opportunityRepository.findAll(pageable)
            .map(opportunityMapper::toDto)
    }

    /**
     * Get all the opportunities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    fun findAllWithEagerRelationships(pageable: Pageable) =
        opportunityRepository.findAllWithEagerRelationships(pageable).map(opportunityMapper::toDto)

    /**
     * Get one opportunity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<OpportunityDTO> {
        log.debug("Request to get Opportunity : {}", id)
        return opportunityRepository.findOneWithEagerRelationships(id)
            .map(opportunityMapper::toDto)
    }

    /**
     * Delete the opportunity by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long) {
        log.debug("Request to delete Opportunity : {}", id)

        opportunityRepository.deleteById(id)
        opportunitySearchRepository.deleteById(id)
    }

    /**
     * Search for the opportunity corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun search(query: String, pageable: Pageable): Page<OpportunityDTO> {
        log.debug("Request to search for a page of Opportunities for query {}", query)
        return opportunitySearchRepository.search(queryStringQuery(query), pageable).map(opportunityMapper::toDto)
    }
}
