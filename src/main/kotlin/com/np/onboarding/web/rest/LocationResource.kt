package com.np.onboarding.web.rest

import com.np.onboarding.service.LocationService
import com.np.onboarding.service.dto.LocationDTO
import com.np.onboarding.web.rest.errors.BadRequestAlertException
import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
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

private const val ENTITY_NAME = "location"
/**
 * REST controller for managing [com.np.onboarding.domain.Location].
 */
@RestController
@RequestMapping("/api")
class LocationResource(
    private val locationService: LocationService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /locations` : Create a new location.
     *
     * @param locationDTO the locationDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new locationDTO, or with status `400 (Bad Request)` if the location has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locations")
    fun createLocation(@RequestBody locationDTO: LocationDTO): ResponseEntity<LocationDTO> {
        log.debug("REST request to save Location : {}", locationDTO)
        if (locationDTO.id != null) {
            throw BadRequestAlertException(
                "A new location cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = locationService.save(locationDTO)
        return ResponseEntity.created(URI("/api/locations/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /locations` : Updates an existing location.
     *
     * @param locationDTO the locationDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated locationDTO,
     * or with status `400 (Bad Request)` if the locationDTO is not valid,
     * or with status `500 (Internal Server Error)` if the locationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locations")
    fun updateLocation(@RequestBody locationDTO: LocationDTO): ResponseEntity<LocationDTO> {
        log.debug("REST request to update Location : {}", locationDTO)
        if (locationDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = locationService.save(locationDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     locationDTO.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /locations` : get all the locations.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of locations in body.
     */
    @GetMapping("/locations")
    fun getAllLocations(): MutableList<LocationDTO> {
        log.debug("REST request to get all Locations")
        return locationService.findAll()
    }

    /**
     * `GET  /locations/:id` : get the "id" location.
     *
     * @param id the id of the locationDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the locationDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/locations/{id}")
    fun getLocation(@PathVariable id: Long): ResponseEntity<LocationDTO> {
        log.debug("REST request to get Location : {}", id)
        val locationDTO = locationService.findOne(id)
        return ResponseUtil.wrapOrNotFound(locationDTO)
    }
    /**
     *  `DELETE  /locations/:id` : delete the "id" location.
     *
     * @param id the id of the locationDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/locations/{id}")
    fun deleteLocation(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Location : {}", id)
        locationService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    /**
     * `SEARCH  /_search/locations?query=:query` : search for the location corresponding
     * to the query.
     *
     * @param query the query of the location search.
     * @return the result of the search.
     */
    @GetMapping("/_search/locations")
    fun searchLocations(@RequestParam query: String): MutableList<LocationDTO> {
        log.debug("REST request to search Locations for query {}", query)
        return locationService.search(query).toMutableList()
    }
}
