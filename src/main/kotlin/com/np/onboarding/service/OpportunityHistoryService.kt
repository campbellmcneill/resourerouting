package com.np.onboarding.service
import com.np.onboarding.service.dto.OpportunityHistoryDTO
import java.util.Optional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Service Interface for managing [com.np.onboarding.domain.OpportunityHistory].
 */
interface OpportunityHistoryService {

    /**
     * Save a opportunityHistory.
     *
     * @param opportunityHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(opportunityHistoryDTO: OpportunityHistoryDTO): OpportunityHistoryDTO

    /**
     * Get all the opportunityHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<OpportunityHistoryDTO>

    /**
     * Get the "id" opportunityHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<OpportunityHistoryDTO>

    /**
     * Delete the "id" opportunityHistory.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)

    /**
     * Search for the opportunityHistory corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun search(query: String, pageable: Pageable): Page<OpportunityHistoryDTO>
}
