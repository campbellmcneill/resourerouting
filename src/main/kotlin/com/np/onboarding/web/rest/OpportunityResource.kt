package com.np.onboarding.web.rest

import com.np.onboarding.service.OpportunityService
import com.np.onboarding.service.dto.OpportunityDTO
import com.np.onboarding.web.rest.errors.BadRequestAlertException
import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
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

private const val ENTITY_NAME = "opportunity"
/**
 * REST controller for managing [com.np.onboarding.domain.Opportunity].
 */
@RestController
@RequestMapping("/api")
class OpportunityResource(
    private val opportunityService: OpportunityService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /opportunities` : Create a new opportunity.
     *
     * @param opportunityDTO the opportunityDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new opportunityDTO, or with status `400 (Bad Request)` if the opportunity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/opportunities")
    fun createOpportunity(@RequestBody opportunityDTO: OpportunityDTO): ResponseEntity<OpportunityDTO> {
        log.debug("REST request to save Opportunity : {}", opportunityDTO)
        if (opportunityDTO.id != null) {
            throw BadRequestAlertException(
                "A new opportunity cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = opportunityService.save(opportunityDTO)
        return ResponseEntity.created(URI("/api/opportunities/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /opportunities` : Updates an existing opportunity.
     *
     * @param opportunityDTO the opportunityDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated opportunityDTO,
     * or with status `400 (Bad Request)` if the opportunityDTO is not valid,
     * or with status `500 (Internal Server Error)` if the opportunityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/opportunities")
    fun updateOpportunity(@RequestBody opportunityDTO: OpportunityDTO): ResponseEntity<OpportunityDTO> {
        log.debug("REST request to update Opportunity : {}", opportunityDTO)
        if (opportunityDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = opportunityService.save(opportunityDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     opportunityDTO.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /opportunities` : get all the opportunities.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the [ResponseEntity] with status `200 (OK)` and the list of opportunities in body.
     */
    @GetMapping("/opportunities")
    fun getAllOpportunities(
        pageable: Pageable,
        @RequestParam(required = false, defaultValue = "false") eagerload: Boolean
    ): ResponseEntity<MutableList<OpportunityDTO>> {
        log.debug("REST request to get a page of Opportunities")
        val page: Page<OpportunityDTO> = if (eagerload) {
            opportunityService.findAllWithEagerRelationships(pageable)
        } else {
            opportunityService.findAll(pageable)
        }
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /opportunities/:id` : get the "id" opportunity.
     *
     * @param id the id of the opportunityDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the opportunityDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/opportunities/{id}")
    fun getOpportunity(@PathVariable id: Long): ResponseEntity<OpportunityDTO> {
        log.debug("REST request to get Opportunity : {}", id)
        val opportunityDTO = opportunityService.findOne(id)
        return ResponseUtil.wrapOrNotFound(opportunityDTO)
    }
    /**
     *  `DELETE  /opportunities/:id` : delete the "id" opportunity.
     *
     * @param id the id of the opportunityDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/opportunities/{id}")
    fun deleteOpportunity(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Opportunity : {}", id)
        opportunityService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    /**
     * `SEARCH  /_search/opportunities?query=:query` : search for the opportunity corresponding
     * to the query.
     *
     * @param query the query of the opportunity search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/opportunities")
    fun searchOpportunities(@RequestParam query: String, pageable: Pageable): ResponseEntity<MutableList<OpportunityDTO>> {
        log.debug("REST request to search for a page of Opportunities for query {}", query)
        val page = opportunityService.search(query, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }
}
