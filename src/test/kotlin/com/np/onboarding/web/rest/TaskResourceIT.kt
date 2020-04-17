package com.np.onboarding.web.rest

import com.np.onboarding.ResourceroutingApp
import com.np.onboarding.domain.Task
import com.np.onboarding.repository.TaskRepository
import com.np.onboarding.repository.search.TaskSearchRepository
import com.np.onboarding.service.TaskService
import com.np.onboarding.service.mapper.TaskMapper
import com.np.onboarding.web.rest.errors.ExceptionTranslator
import javax.persistence.EntityManager
import kotlin.test.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator

/**
 * Integration tests for the [TaskResource] REST controller.
 *
 * @see TaskResource
 */
@SpringBootTest(classes = [ResourceroutingApp::class])
class TaskResourceIT {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Autowired
    private lateinit var taskMapper: TaskMapper

    @Autowired
    private lateinit var taskService: TaskService

    /**
     * This repository is mocked in the com.np.onboarding.repository.search test package.
     *
     * @see com.np.onboarding.repository.search.TaskSearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockTaskSearchRepository: TaskSearchRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restTaskMockMvc: MockMvc

    private lateinit var task: Task

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val taskResource = TaskResource(taskService)
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        task = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createTask() {
        val databaseSizeBeforeCreate = taskRepository.findAll().size

        // Create the Task
        val taskDTO = taskMapper.toDto(task)
        restTaskMockMvc.perform(
            post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(taskDTO))
        ).andExpect(status().isCreated)

        // Validate the Task in the database
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1)
        val testTask = taskList[taskList.size - 1]
        assertThat(testTask.title).isEqualTo(DEFAULT_TITLE)
        assertThat(testTask.description).isEqualTo(DEFAULT_DESCRIPTION)

        // Validate the Task in Elasticsearch
    }

    @Test
    @Transactional
    fun createTaskWithExistingId() {
        val databaseSizeBeforeCreate = taskRepository.findAll().size

        // Create the Task with an existing ID
        task.id = 1L
        val taskDTO = taskMapper.toDto(task)

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc.perform(
            post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(taskDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Task in the database
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeCreate)

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(0)).save(task)
    }

    @Test
    @Transactional
    fun getAllTasks() {
        // Initialize the database
        taskRepository.saveAndFlush(task)

        // Get all the taskList
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
    }

    @Test
    @Transactional
    fun getTask() {
        // Initialize the database
        taskRepository.saveAndFlush(task)

        val id = task.id
        assertNotNull(id)

        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
    }

    @Test
    @Transactional
    fun getNonExistingTask() {
        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun updateTask() {
        // Initialize the database
        taskRepository.saveAndFlush(task)

        val databaseSizeBeforeUpdate = taskRepository.findAll().size

        // Update the task
        val id = task.id
        assertNotNull(id)
        val updatedTask = taskRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask)
        updatedTask.title = UPDATED_TITLE
        updatedTask.description = UPDATED_DESCRIPTION
        val taskDTO = taskMapper.toDto(updatedTask)

        restTaskMockMvc.perform(
            put("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(taskDTO))
        ).andExpect(status().isOk)

        // Validate the Task in the database
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate)
        val testTask = taskList[taskList.size - 1]
        assertThat(testTask.title).isEqualTo(UPDATED_TITLE)
        assertThat(testTask.description).isEqualTo(UPDATED_DESCRIPTION)

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(1)).save(testTask)
    }

    @Test
    @Transactional
    fun updateNonExistingTask() {
        val databaseSizeBeforeUpdate = taskRepository.findAll().size

        // Create the Task
        val taskDTO = taskMapper.toDto(task)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc.perform(
            put("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(taskDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Task in the database
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(0)).save(task)
    }

    @Test
    @Transactional
    fun deleteTask() {
        // Initialize the database
        taskRepository.saveAndFlush(task)

        val databaseSizeBeforeDelete = taskRepository.findAll().size

        val id = task.id
        assertNotNull(id)

        // Delete the task
        restTaskMockMvc.perform(
            delete("/api/tasks/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun searchTask() {
        // InitializesearchTask() the database
        taskRepository.saveAndFlush(task)
        `when`(mockTaskSearchRepository.search(queryStringQuery("id:" + task.id)))
            .thenReturn(listOf(task))
        // Search the task
        restTaskMockMvc.perform(get("/api/_search/tasks?query=id:" + task.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.id?.toInt())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
    }

    companion object {

        private const val DEFAULT_TITLE = "AAAAAAAAAA"
        private const val UPDATED_TITLE = "BBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Task {
            val task = Task(
                title = DEFAULT_TITLE,
                description = DEFAULT_DESCRIPTION
            )

            return task
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Task {
            val task = Task(
                title = UPDATED_TITLE,
                description = UPDATED_DESCRIPTION
            )

            return task
        }
    }
}
