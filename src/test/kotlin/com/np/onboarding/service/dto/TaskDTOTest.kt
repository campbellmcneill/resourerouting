package com.np.onboarding.service.dto

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TaskDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(TaskDTO::class)
        val taskDTO1 = TaskDTO()
        taskDTO1.id = 1L
        val taskDTO2 = TaskDTO()
        assertThat(taskDTO1).isNotEqualTo(taskDTO2)
        taskDTO2.id = taskDTO1.id
        assertThat(taskDTO1).isEqualTo(taskDTO2)
        taskDTO2.id = 2L
        assertThat(taskDTO1).isNotEqualTo(taskDTO2)
        taskDTO1.id = null
        assertThat(taskDTO1).isNotEqualTo(taskDTO2)
    }
}
