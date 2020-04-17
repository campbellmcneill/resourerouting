package com.np.onboarding.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TaskMapperTest {

    private lateinit var taskMapper: TaskMapper

    @BeforeEach
    fun setUp() {
        taskMapper = TaskMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = 1L
        assertThat(taskMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(taskMapper.fromId(null)).isNull()
    }
}
