package com.np.onboarding.domain

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OpportunityHistoryTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(OpportunityHistory::class)
        val opportunityHistory1 = OpportunityHistory()
        opportunityHistory1.id = 1L
        val opportunityHistory2 = OpportunityHistory()
        opportunityHistory2.id = opportunityHistory1.id
        assertThat(opportunityHistory1).isEqualTo(opportunityHistory2)
        opportunityHistory2.id = 2L
        assertThat(opportunityHistory1).isNotEqualTo(opportunityHistory2)
        opportunityHistory1.id = null
        assertThat(opportunityHistory1).isNotEqualTo(opportunityHistory2)
    }
}
