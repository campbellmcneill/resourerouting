package com.np.onboarding.service.dto

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RegionDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(RegionDTO::class)
        val regionDTO1 = RegionDTO()
        regionDTO1.id = 1L
        val regionDTO2 = RegionDTO()
        assertThat(regionDTO1).isNotEqualTo(regionDTO2)
        regionDTO2.id = regionDTO1.id
        assertThat(regionDTO1).isEqualTo(regionDTO2)
        regionDTO2.id = 2L
        assertThat(regionDTO1).isNotEqualTo(regionDTO2)
        regionDTO1.id = null
        assertThat(regionDTO1).isNotEqualTo(regionDTO2)
    }
}
