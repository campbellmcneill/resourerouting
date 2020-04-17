package com.np.onboarding.web.rest

import com.np.onboarding.service.OpportunityHistoryService
import com.np.onboarding.service.dto.OpportunityHistoryDTO
import com.np.onboarding.web.rest.errors.BadRequestAlertException
import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

private const val ENTITY_NAME = "opportunityHistory"
/**
 * REST controller for managing [com.np.onboarding.domain.OpportunityHistory].
 */
@RestController
@RequestMapping("/api")
class OpportunityHistoryResource(
    private val opportunityHistoryService: OpportunityHistoryService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /opportunity-histories` : Create a new opportunityHistory.
     *
     * @param opportunityHistoryDTO the opportunityHistoryDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new opportunityHistoryDTO, or with status `400 (Bad Request)` if the opportunityHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/opportunity-histories")
    fun createOpportunityHistory(@RequestBody opportunityHistoryDTO: OpportunityHistoryDTO): ResponseEntity<OpportunityHistoryDTO> {
        log.debug("REST request to save OpportunityHistory : {}", opportunityHistoryDTO)
        if (opportunityHistoryDTO.id != null) {
            throw BadRequestAlertException(
                "A new opportunityHistory cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = opportunityHistoryService.save(opportunityHistoryDTO)
        return ResponseEntity.created(URI("/api/opportunity-histories/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /opportunity-histories` : Updates an existing opportunityHistory.
     *
     * @param opportunityHistoryDTO the opportunityHistoryDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated opportunityHistoryDTO,
     * or with status `400 (Bad Request)` if the opportunityHistoryDTO is not valid,
     * or with status `500 (Internal Server Error)` if the opportunityHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/opportunity-histories")
    fun updateOpportunityHistory(@RequestBody opportunityHistoryDTO: OpportunityHistoryDTO): ResponseEntity<OpportunityHistoryDTO> {
        log.debug("REST request to update OpportunityHistory : {}", opportunityHistoryDTO)
        if (opportunityHistoryDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = opportunityHistoryService.save(opportunityHistoryDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     opportunityHistoryDTO.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /opportunity-histories` : get all the opportunityHistories.
     *
     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of opportunityHistories in body.
     */
    @GetMapping("/opportunity-histories")
    fun getAllOpportunityHistories(
        pageable: Pageable
    ): ResponseEntity<MutableList<OpportunityHistoryDTO>> {
        log.debug("REST request to get a page of OpportunityHistories")
        val page = opportunityHistoryService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /opportunity-histories/:id` : get the "id" opportunityHistory.
     *
     * @param id the id of the opportunityHistoryDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the opportunityHistoryDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/opportunity-histories/{id}")
    fun getOpportunityHistory(@PathVariable id: Long): ResponseEntity<OpportunityHistoryDTO> {
        log.debug("REST request to get OpportunityHistory : {}", id)
        val opportunityHistoryDTO = opportunityHistoryService.findOne(id)
        return ResponseUtil.wrapOrNotFound(opportunityHistoryDTO)
    }
    /**
     *  `DELETE  /opportunity-histories/:id` : delete the "id" opportunityHistory.
     *
     * @param id the id of the opportunityHistoryDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/opportunity-histories/{id}")
    fun deleteOpportunityHistory(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete OpportunityHistory : {}", id)
        opportunityHistoryService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    /**
     * `SEARCH  /_search/opportunity-histories?query=:query` : search for the opportunityHistory corresponding
     * to the query.
     *
     * @param query the query of the opportunityHistory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/opportunity-histories")
    fun searchOpportunityHistories(@RequestParam query: String, pageable: Pageable): ResponseEntity<MutableList<OpportunityHistoryDTO>> {
        log.debug("REST request to search for a page of OpportunityHistories for query {}", query)
        val page = opportunityHistoryService.search(query, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }
}
