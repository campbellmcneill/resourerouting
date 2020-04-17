package com.np.onboarding.service.dto

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LocationDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(LocationDTO::class)
        val locationDTO1 = LocationDTO()
        locationDTO1.id = 1L
        val locationDTO2 = LocationDTO()
        assertThat(locationDTO1).isNotEqualTo(locationDTO2)
        locationDTO2.id = locationDTO1.id
        assertThat(locationDTO1).isEqualTo(locationDTO2)
        locationDTO2.id = 2L
        assertThat(locationDTO1).isNotEqualTo(locationDTO2)
        locationDTO1.id = null
        assertThat(locationDTO1).isNotEqualTo(locationDTO2)
    }
}
