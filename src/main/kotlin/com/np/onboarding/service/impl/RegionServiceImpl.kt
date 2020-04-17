package com.np.onboarding.service.impl

import com.np.onboarding.domain.Region
import com.np.onboarding.repository.RegionRepository
import com.np.onboarding.repository.search.RegionSearchRepository
import com.np.onboarding.service.RegionService
import com.np.onboarding.service.dto.RegionDTO
import com.np.onboarding.service.mapper.RegionMapper
import java.util.Optional
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service Implementation for managing [Region].
 */
@Service
@Transactional
class RegionServiceImpl(
    private val regionRepository: RegionRepository,
    private val regionMapper: RegionMapper,
    private val regionSearchRepository: RegionSearchRepository
) : RegionService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a region.
     *
     * @param regionDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(regionDTO: RegionDTO): RegionDTO {
        log.debug("Request to save Region : {}", regionDTO)

        var region = regionMapper.toEntity(regionDTO)
        region = regionRepository.save(region)
        val result = regionMapper.toDto(region)
        regionSearchRepository.save(region)
        return result
    }

    /**
     * Get all the regions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(): MutableList<RegionDTO> {
        log.debug("Request to get all Regions")
        return regionRepository.findAll()
            .mapTo(mutableListOf(), regionMapper::toDto)
    }

    /**
     * Get one region by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<RegionDTO> {
        log.debug("Request to get Region : {}", id)
        return regionRepository.findById(id)
            .map(regionMapper::toDto)
    }

    /**
     * Delete the region by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Region : {}", id)

        regionRepository.deleteById(id)
        regionSearchRepository.deleteById(id)
    }

    /**
     * Search for the region corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun search(query: String): MutableList<RegionDTO> {
        log.debug("Request to search Regions for query {}", query)
        return regionSearchRepository.search(queryStringQuery(query))
            .mapTo(mutableListOf(), regionMapper::toDto)
    }
}
