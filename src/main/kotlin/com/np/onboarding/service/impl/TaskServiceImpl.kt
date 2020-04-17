package com.np.onboarding.service.impl

import com.np.onboarding.domain.Task
import com.np.onboarding.repository.TaskRepository
import com.np.onboarding.repository.search.TaskSearchRepository
import com.np.onboarding.service.TaskService
import com.np.onboarding.service.dto.TaskDTO
import com.np.onboarding.service.mapper.TaskMapper
import java.util.Optional
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service Implementation for managing [Task].
 */
@Service
@Transactional
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    private val taskMapper: TaskMapper,
    private val taskSearchRepository: TaskSearchRepository
) : TaskService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a task.
     *
     * @param taskDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(taskDTO: TaskDTO): TaskDTO {
        log.debug("Request to save Task : {}", taskDTO)

        var task = taskMapper.toEntity(taskDTO)
        task = taskRepository.save(task)
        val result = taskMapper.toDto(task)
        taskSearchRepository.save(task)
        return result
    }

    /**
     * Get all the tasks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(): MutableList<TaskDTO> {
        log.debug("Request to get all Tasks")
        return taskRepository.findAll()
            .mapTo(mutableListOf(), taskMapper::toDto)
    }

    /**
     * Get one task by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<TaskDTO> {
        log.debug("Request to get Task : {}", id)
        return taskRepository.findById(id)
            .map(taskMapper::toDto)
    }

    /**
     * Delete the task by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: Long) {
        log.debug("Request to delete Task : {}", id)

        taskRepository.deleteById(id)
        taskSearchRepository.deleteById(id)
    }

    /**
     * Search for the task corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun search(query: String): MutableList<TaskDTO> {
        log.debug("Request to search Tasks for query {}", query)
        return taskSearchRepository.search(queryStringQuery(query))
            .mapTo(mutableListOf(), taskMapper::toDto)
    }
}
