package com.np.onboarding.service
import com.np.onboarding.service.dto.TaskDTO
import java.util.Optional

/**
 * Service Interface for managing [com.np.onboarding.domain.Task].
 */
interface TaskService {

    /**
     * Save a task.
     *
     * @param taskDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(taskDTO: TaskDTO): TaskDTO

    /**
     * Get all the tasks.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<TaskDTO>

    /**
     * Get the "id" task.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<TaskDTO>

    /**
     * Delete the "id" task.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)

    /**
     * Search for the task corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    fun search(query: String): MutableList<TaskDTO>
}
