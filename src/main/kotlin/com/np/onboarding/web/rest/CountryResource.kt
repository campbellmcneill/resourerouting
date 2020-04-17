package com.np.onboarding.web.rest

import com.np.onboarding.service.CountryService
import com.np.onboarding.service.dto.CountryDTO
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

private const val ENTITY_NAME = "country"
/**
 * REST controller for managing [com.np.onboarding.domain.Country].
 */
@RestController
@RequestMapping("/api")
class CountryResource(
    private val countryService: CountryService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /countries` : Create a new country.
     *
     * @param countryDTO the countryDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new countryDTO, or with status `400 (Bad Request)` if the country has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/countries")
    fun createCountry(@RequestBody countryDTO: CountryDTO): ResponseEntity<CountryDTO> {
        log.debug("REST request to save Country : {}", countryDTO)
        if (countryDTO.id != null) {
            throw BadRequestAlertException(
                "A new country cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = countryService.save(countryDTO)
        return ResponseEntity.created(URI("/api/countries/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /countries` : Updates an existing country.
     *
     * @param countryDTO the countryDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated countryDTO,
     * or with status `400 (Bad Request)` if the countryDTO is not valid,
     * or with status `500 (Internal Server Error)` if the countryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/countries")
    fun updateCountry(@RequestBody countryDTO: CountryDTO): ResponseEntity<CountryDTO> {
        log.debug("REST request to update Country : {}", countryDTO)
        if (countryDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = countryService.save(countryDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     countryDTO.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /countries` : get all the countries.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of countries in body.
     */
    @GetMapping("/countries")
    fun getAllCountries(): MutableList<CountryDTO> {
        log.debug("REST request to get all Countries")
        return countryService.findAll()
    }

    /**
     * `GET  /countries/:id` : get the "id" country.
     *
     * @param id the id of the countryDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the countryDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/countries/{id}")
    fun getCountry(@PathVariable id: Long): ResponseEntity<CountryDTO> {
        log.debug("REST request to get Country : {}", id)
        val countryDTO = countryService.findOne(id)
        return ResponseUtil.wrapOrNotFound(countryDTO)
    }
    /**
     *  `DELETE  /countries/:id` : delete the "id" country.
     *
     * @param id the id of the countryDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/countries/{id}")
    fun deleteCountry(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Country : {}", id)
        countryService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    /**
     * `SEARCH  /_search/countries?query=:query` : search for the country corresponding
     * to the query.
     *
     * @param query the query of the country search.
     * @return the result of the search.
     */
    @GetMapping("/_search/countries")
    fun searchCountries(@RequestParam query: String): MutableList<CountryDTO> {
        log.debug("REST request to search Countries for query {}", query)
        return countryService.search(query).toMutableList()
    }
}
