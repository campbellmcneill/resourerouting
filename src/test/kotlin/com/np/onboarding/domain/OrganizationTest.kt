package com.np.onboarding.domain

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrganizationTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Organization::class)
        val organization1 = Organization()
        organization1.id = 1L
        val organization2 = Organization()
        organization2.id = organization1.id
        assertThat(organization1).isEqualTo(organization2)
        organization2.id = 2L
        assertThat(organization1).isNotEqualTo(organization2)
        organization1.id = null
        assertThat(organization1).isNotEqualTo(organization2)
    }
}
