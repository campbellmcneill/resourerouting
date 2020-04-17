package com.np.onboarding.service.dto

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrganizationDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(OrganizationDTO::class)
        val organizationDTO1 = OrganizationDTO()
        organizationDTO1.id = 1L
        val organizationDTO2 = OrganizationDTO()
        assertThat(organizationDTO1).isNotEqualTo(organizationDTO2)
        organizationDTO2.id = organizationDTO1.id
        assertThat(organizationDTO1).isEqualTo(organizationDTO2)
        organizationDTO2.id = 2L
        assertThat(organizationDTO1).isNotEqualTo(organizationDTO2)
        organizationDTO1.id = null
        assertThat(organizationDTO1).isNotEqualTo(organizationDTO2)
    }
}
