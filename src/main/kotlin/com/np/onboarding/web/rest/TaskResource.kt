package com.np.onboarding.web.rest

import com.np.onboarding.service.TaskService
import com.np.onboarding.service.dto.TaskDTO
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

private const val ENTITY_NAME = "task"
/**
 * REST controller for managing [com.np.onboarding.domain.Task].
 */
@RestController
@RequestMapping("/api")
class TaskResource(
    private val taskService: TaskService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /tasks` : Create a new task.
     *
     * @param taskDTO the taskDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new taskDTO, or with status `400 (Bad Request)` if the task has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tasks")
    fun createTask(@RequestBody taskDTO: TaskDTO): ResponseEntity<TaskDTO> {
        log.debug("REST request to save Task : {}", taskDTO)
        if (taskDTO.id != null) {
            throw BadRequestAlertException(
                "A new task cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = taskService.save(taskDTO)
        return ResponseEntity.created(URI("/api/tasks/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /tasks` : Updates an existing task.
     *
     * @param taskDTO the taskDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated taskDTO,
     * or with status `400 (Bad Request)` if the taskDTO is not valid,
     * or with status `500 (Internal Server Error)` if the taskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tasks")
    fun updateTask(@RequestBody taskDTO: TaskDTO): ResponseEntity<TaskDTO> {
        log.debug("REST request to update Task : {}", taskDTO)
        if (taskDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = taskService.save(taskDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     taskDTO.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /tasks` : get all the tasks.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of tasks in body.
     */
    @GetMapping("/tasks")
    fun getAllTasks(): MutableList<TaskDTO> {
        log.debug("REST request to get all Tasks")
        return taskService.findAll()
    }

    /**
     * `GET  /tasks/:id` : get the "id" task.
     *
     * @param id the id of the taskDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the taskDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/tasks/{id}")
    fun getTask(@PathVariable id: Long): ResponseEntity<TaskDTO> {
        log.debug("REST request to get Task : {}", id)
        val taskDTO = taskService.findOne(id)
        return ResponseUtil.wrapOrNotFound(taskDTO)
    }
    /**
     *  `DELETE  /tasks/:id` : delete the "id" task.
     *
     * @param id the id of the taskDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/tasks/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Task : {}", id)
        taskService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }

    /**
     * `SEARCH  /_search/tasks?query=:query` : search for the task corresponding
     * to the query.
     *
     * @param query the query of the task search.
     * @return the result of the search.
     */
    @GetMapping("/_search/tasks")
    fun searchTasks(@RequestParam query: String): MutableList<TaskDTO> {
        log.debug("REST request to search Tasks for query {}", query)
        return taskService.search(query).toMutableList()
    }
}
