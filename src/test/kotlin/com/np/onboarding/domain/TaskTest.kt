package com.np.onboarding.domain

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TaskTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Task::class)
        val task1 = Task()
        task1.id = 1L
        val task2 = Task()
        task2.id = task1.id
        assertThat(task1).isEqualTo(task2)
        task2.id = 2L
        assertThat(task1).isNotEqualTo(task2)
        task1.id = null
        assertThat(task1).isNotEqualTo(task2)
    }
}
