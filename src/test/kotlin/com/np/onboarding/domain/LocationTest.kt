package com.np.onboarding.domain

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LocationTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Location::class)
        val location1 = Location()
        location1.id = 1L
        val location2 = Location()
        location2.id = location1.id
        assertThat(location1).isEqualTo(location2)
        location2.id = 2L
        assertThat(location1).isNotEqualTo(location2)
        location1.id = null
        assertThat(location1).isNotEqualTo(location2)
    }
}
