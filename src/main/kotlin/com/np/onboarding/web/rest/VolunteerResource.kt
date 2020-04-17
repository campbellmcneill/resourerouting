package com.np.onboarding.web.rest

import com.np.onboarding.service.VolunteerService
import com.np.onboarding.service.dto.VolunteerDTO
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

private const val ENTITY_NAME = "volunteer"
/**
 * REST controller for managing [com.np.onboarding.domain.Volunteer].
 */
@RestController
@RequestMapping("/api")
class VolunteerResource(
    private val volunteerService: VolunteerService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /volunteers` : Create a new volunteer.
     *
     * @param volunteerDTO the volunteerDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new volunteerDTO, or with status `400 (Bad Request)` if the volunteer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/volunteers")
    fun createVolunteer(@RequestBody volunteerDTO: VolunteerDTO): ResponseEntity<VolunteerDTO> {
        log.debug("REST request to save Volunteer : {}", volunteerDTO)
        if (volunteerDTO.id != null) {
            throw BadRequestAlertException(
                "A new volunteer cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = volunteerService.save(volunteerDTO)
        return ResponseEntity.created(URI("/api/volunteers/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /volunteers` : Updates an existing volunteer.
     *
     * @param volunteerDTO the volunteerDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated volunteerDTO,
     * or with status `400 (Bad Request)` if the volunteerDTO is not valid,
     * or with status `500 (Internal Server Error)` if the volunteerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/volunteers")
    fun updateVolunteer(@RequestBody volunteerDTO: VolunteerDTO): ResponseEntity<VolunteerDTO> {
        log.debug("REST request to update Volunteer : {}", volunteerDTO)
        if (volunteerDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = volunteerService.save(volunteerDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     volunteerDTO.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /volunteers` : get all the volunteers.
     *
     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of volunteers in body.
     */
    @GetMapping("/volunteers")
    fun getAllVolunteers(
        pageable: Pageable
    ): ResponseEntity<MutableList<VolunteerDTO>> {
        log.debug("REST request to get a page of Volunteers")
        val page = volunteerService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /volunteers/:id` : get the "id" volunteer.
     *
     * @param id the id of the volunteerDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the volunteerDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/volunteers/{id}")
    fun getVolunteer(@PathVariable id: Long): ResponseEntity<VolunteerDTO> {
        log.debug("REST request to get Volunteer : {}", id)
        val volunteerDTO = volunteerService.findOne(id)
        return ResponseUtil.wrapOrNotFound(volunteerDTO)
    }
    /**
     *  `DELETE  /volunteers/:id` : delete the "id" volunteer.
     *
     * @param id the id of the volunteerDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/volunteers/{id}")
    fun deleteVolunteer(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Volunteer : {}", id)
        volunteerService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    /**
     * `SEARCH  /_search/volunteers?query=:query` : search for the volunteer corresponding
     * to the query.
     *
     * @param query the query of the volunteer search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/volunteers")
    fun searchVolunteers(@RequestParam query: String, pageable: Pageable): ResponseEntity<MutableList<VolunteerDTO>> {
        log.debug("REST request to search for a page of Volunteers for query {}", query)
        val page = volunteerService.search(query, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }
}
