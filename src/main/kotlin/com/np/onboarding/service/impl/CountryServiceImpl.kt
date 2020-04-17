package com.np.onboarding.service.impl

import com.np.onboarding.domain.Country
import com.np.onboarding.repository.CountryRepository
import com.np.onboarding.repository.search.CountrySearchRepository
import com.np.onboarding.service.CountryService
import com.np.onboarding.service.dto.CountryDTO
import com.np.onboarding.service.mapper.CountryMapper
import java.util.Optional
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service Implementation for managing [Country].
 */
@Service
@Transactional
class CountryServiceImpl(
    private val countryRepository: CountryRepository,
    private val countryMapper: CountryMapper,
    private val countrySearchRepository: CountrySearchRepository
) : CountryService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a country.
     *
     * @param countryDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(countryDTO: CountryDTO): CountryDTO {
        log.debug("Request to save Country : {}", countryDTO)

        var country = countryMapper.toEntity(countryDTO)
        country = countryRepository.save(country)
        val result = countryMapper.toDto(country)
        countrySearchRepository.save(country)
        return result
    }

    /**
     * Get all the countries.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(): MutableList<CountryDTO> {
        log.debug("Request to get all Countries")
        return countryRepository.findAll()
            .mapTo(mutableListOf(), countryMapper::toDto)
    }

    /**
     * Get one country by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<CountryDTO> {
        log.debug("Request to get Country : {}", id)
        return countryRepository.findById(id)
            .map(countryMapper::toDto)
    }

    /**
     * Delete the country by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Country : {}", id)

        countryRepository.deleteById(id)
        countrySearchRepository.deleteById(id)
    }

    /**
     * Search for the country corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun search(query: String): MutableList<CountryDTO> {
        log.debug("Request to search Countries for query {}", query)
        return countrySearchRepository.search(queryStringQuery(query))
            .mapTo(mutableListOf(), countryMapper::toDto)
    }
}
