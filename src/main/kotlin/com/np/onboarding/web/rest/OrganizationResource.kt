package com.np.onboarding.web.rest

import com.np.onboarding.service.OrganizationService
import com.np.onboarding.service.dto.OrganizationDTO
import com.np.onboarding.web.rest.errors.BadRequestAlertException
import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import javax.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
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

private const val ENTITY_NAME = "organization"
/**
 * REST controller for managing [com.np.onboarding.domain.Organization].
 */
@RestController
@RequestMapping("/api")
class OrganizationResource(
    private val organizationService: OrganizationService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /organizations` : Create a new organization.
     *
     * @param organizationDTO the organizationDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new organizationDTO, or with status `400 (Bad Request)` if the organization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organizations")
    fun createOrganization(@Valid @RequestBody organizationDTO: OrganizationDTO): ResponseEntity<OrganizationDTO> {
        log.debug("REST request to save Organization : {}", organizationDTO)
        if (organizationDTO.id != null) {
            throw BadRequestAlertException(
                "A new organization cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = organizationService.save(organizationDTO)
        return ResponseEntity.created(URI("/api/organizations/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /organizations` : Updates an existing organization.
     *
     * @param organizationDTO the organizationDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated organizationDTO,
     * or with status `400 (Bad Request)` if the organizationDTO is not valid,
     * or with status `500 (Internal Server Error)` if the organizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organizations")
    fun updateOrganization(@Valid @RequestBody organizationDTO: OrganizationDTO): ResponseEntity<OrganizationDTO> {
        log.debug("REST request to update Organization : {}", organizationDTO)
        if (organizationDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = organizationService.save(organizationDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     organizationDTO.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /organizations` : get all the organizations.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of organizations in body.
     */
    @GetMapping("/organizations")
    fun getAllOrganizations(): MutableList<OrganizationDTO> {
        log.debug("REST request to get all Organizations")
        return organizationService.findAll()
    }

    /**
     * `GET  /organizations/:id` : get the "id" organization.
     *
     * @param id the id of the organizationDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the organizationDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/organizations/{id}")
    fun getOrganization(@PathVariable id: Long): ResponseEntity<OrganizationDTO> {
        log.debug("REST request to get Organization : {}", id)
        val organizationDTO = organizationService.findOne(id)
        return ResponseUtil.wrapOrNotFound(organizationDTO)
    }
    /**
     *  `DELETE  /organizations/:id` : delete the "id" organization.
     *
     * @param id the id of the organizationDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/organizations/{id}")
    fun deleteOrganization(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Organization : {}", id)
        organizationService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    /**
     * `SEARCH  /_search/organizations?query=:query` : search for the organization corresponding
     * to the query.
     *
     * @param query the query of the organization search.
     * @return the result of the search.
     */
    @GetMapping("/_search/organizations")
    fun searchOrganizations(@RequestParam query: String): MutableList<OrganizationDTO> {
        log.debug("REST request to search Organizations for query {}", query)
        return organizationService.search(query).toMutableList()
    }
}
