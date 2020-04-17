package com.np.onboarding.service
import com.np.onboarding.domain.Volunteer
import com.np.onboarding.repository.VolunteerRepository
import com.np.onboarding.repository.search.VolunteerSearchRepository
import com.np.onboarding.service.dto.VolunteerDTO
import com.np.onboarding.service.mapper.VolunteerMapper
import java.util.Optional
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service Implementation for managing [Volunteer].
 */
@Service
@Transactional
class VolunteerService(
    private val volunteerRepository: VolunteerRepository,
    private val volunteerMapper: VolunteerMapper,
    private val volunteerSearchRepository: VolunteerSearchRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a volunteer.
     *
     * @param volunteerDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(volunteerDTO: VolunteerDTO): VolunteerDTO {
        log.debug("Request to save Volunteer : {}", volunteerDTO)

        var volunteer = volunteerMapper.toEntity(volunteerDTO)
        volunteer = volunteerRepository.save(volunteer)
        val result = volunteerMapper.toDto(volunteer)
        volunteerSearchRepository.save(volunteer)
        return result
    }

    /**
     * Get all the volunteers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<VolunteerDTO> {
        log.debug("Request to get all Volunteers")
        return volunteerRepository.findAll(pageable)
            .map(volunteerMapper::toDto)
    }

    /**
     * Get one volunteer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    fun findOne(id: Long): Optional<VolunteerDTO> {
        log.debug("Request to get Volunteer : {}", id)
        return volunteerRepository.findById(id)
            .map(volunteerMapper::toDto)
    }

    /**
     * Delete the volunteer by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long) {
        log.debug("Request to delete Volunteer : {}", id)

        volunteerRepository.deleteById(id)
        volunteerSearchRepository.deleteById(id)
    }

    /**
     * Search for the volunteer corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    fun search(query: String, pageable: Pageable): Page<VolunteerDTO> {
        log.debug("Request to search for a page of Volunteers for query {}", query)
        return volunteerSearchRepository.search(queryStringQuery(query), pageable).map(volunteerMapper::toDto)
    }
}
