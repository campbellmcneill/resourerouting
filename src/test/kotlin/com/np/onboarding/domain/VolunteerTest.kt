package com.np.onboarding.domain

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class VolunteerTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Volunteer::class)
        val volunteer1 = Volunteer()
        volunteer1.id = 1L
        val volunteer2 = Volunteer()
        volunteer2.id = volunteer1.id
        assertThat(volunteer1).isEqualTo(volunteer2)
        volunteer2.id = 2L
        assertThat(volunteer1).isNotEqualTo(volunteer2)
        volunteer1.id = null
        assertThat(volunteer1).isNotEqualTo(volunteer2)
    }
}
