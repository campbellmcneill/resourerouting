package com.np.onboarding.domain

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OpportunityTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Opportunity::class)
        val opportunity1 = Opportunity()
        opportunity1.id = 1L
        val opportunity2 = Opportunity()
        opportunity2.id = opportunity1.id
        assertThat(opportunity1).isEqualTo(opportunity2)
        opportunity2.id = 2L
        assertThat(opportunity1).isNotEqualTo(opportunity2)
        opportunity1.id = null
        assertThat(opportunity1).isNotEqualTo(opportunity2)
    }
}
