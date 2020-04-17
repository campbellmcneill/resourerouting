package com.np.onboarding.service
import com.np.onboarding.service.dto.CountryDTO
import java.util.Optional

/**
 * Service Interface for managing [com.np.onboarding.domain.Country].
 */
interface CountryService {

    /**
     * Save a country.
     *
     * @param countryDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(countryDTO: CountryDTO): CountryDTO

    /**
     * Get all the countries.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<CountryDTO>

    /**
     * Get the "id" country.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<CountryDTO>

    /**
     * Delete the "id" country.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)

    /**
     * Search for the country corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    fun search(query: String): MutableList<CountryDTO>
}
