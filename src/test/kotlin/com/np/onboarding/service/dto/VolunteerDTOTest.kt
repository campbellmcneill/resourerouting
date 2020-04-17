package com.np.onboarding.service.dto

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class VolunteerDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(VolunteerDTO::class)
        val volunteerDTO1 = VolunteerDTO()
        volunteerDTO1.id = 1L
        val volunteerDTO2 = VolunteerDTO()
        assertThat(volunteerDTO1).isNotEqualTo(volunteerDTO2)
        volunteerDTO2.id = volunteerDTO1.id
        assertThat(volunteerDTO1).isEqualTo(volunteerDTO2)
        volunteerDTO2.id = 2L
        assertThat(volunteerDTO1).isNotEqualTo(volunteerDTO2)
        volunteerDTO1.id = null
        assertThat(volunteerDTO1).isNotEqualTo(volunteerDTO2)
    }
}
