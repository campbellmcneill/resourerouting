package com.np.onboarding.service.impl

import com.np.onboarding.domain.OpportunityHistory
import com.np.onboarding.repository.OpportunityHistoryRepository
import com.np.onboarding.repository.search.OpportunityHistorySearchRepository
import com.np.onboarding.service.OpportunityHistoryService
import com.np.onboarding.service.dto.OpportunityHistoryDTO
import com.np.onboarding.service.mapper.OpportunityHistoryMapper
import java.util.Optional
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service Implementation for managing [OpportunityHistory].
 */
@Service
@Transactional
class OpportunityHistoryServiceImpl(
    private val opportunityHistoryRepository: OpportunityHistoryRepository,
    private val opportunityHistoryMapper: OpportunityHistoryMapper,
    private val opportunityHistorySearchRepository: OpportunityHistorySearchRepository
) : OpportunityHistoryService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a opportunityHistory.
     *
     * @param opportunityHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(opportunityHistoryDTO: OpportunityHistoryDTO): OpportunityHistoryDTO {
        log.debug("Request to save OpportunityHistory : {}", opportunityHistoryDTO)

        var opportunityHistory = opportunityHistoryMapper.toEntity(opportunityHistoryDTO)
        opportunityHistory = opportunityHistoryRepository.save(opportunityHistory)
        val result = opportunityHistoryMapper.toDto(opportunityHistory)
        opportunityHistorySearchRepository.save(opportunityHistory)
        return result
    }

    /**
     * Get all the opportunityHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<OpportunityHistoryDTO> {
        log.debug("Request to get all OpportunityHistories")
        return opportunityHistoryRepository.findAll(pageable)
            .map(opportunityHistoryMapper::toDto)
    }

    /**
     * Get one opportunityHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<OpportunityHistoryDTO> {
        log.debug("Request to get OpportunityHistory : {}", id)
        return opportunityHistoryRepository.findById(id)
            .map(opportunityHistoryMapper::toDto)
    }

    /**
     * Delete the opportunityHistory by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete OpportunityHistory : {}", id)

        opportunityHistoryRepository.deleteById(id)
        opportunityHistorySearchRepository.deleteById(id)
    }

    /**
     * Search for the opportunityHistory corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun search(query: String, pageable: Pageable): Page<OpportunityHistoryDTO> {
        log.debug("Request to search for a page of OpportunityHistories for query {}", query)
        return opportunityHistorySearchRepository.search(queryStringQuery(query), pageable).map(opportunityHistoryMapper::toDto)
    }
}
