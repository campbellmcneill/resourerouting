package com.np.onboarding.service.dto

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OpportunityHistoryDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(OpportunityHistoryDTO::class)
        val opportunityHistoryDTO1 = OpportunityHistoryDTO()
        opportunityHistoryDTO1.id = 1L
        val opportunityHistoryDTO2 = OpportunityHistoryDTO()
        assertThat(opportunityHistoryDTO1).isNotEqualTo(opportunityHistoryDTO2)
        opportunityHistoryDTO2.id = opportunityHistoryDTO1.id
        assertThat(opportunityHistoryDTO1).isEqualTo(opportunityHistoryDTO2)
        opportunityHistoryDTO2.id = 2L
        assertThat(opportunityHistoryDTO1).isNotEqualTo(opportunityHistoryDTO2)
        opportunityHistoryDTO1.id = null
        assertThat(opportunityHistoryDTO1).isNotEqualTo(opportunityHistoryDTO2)
    }
}
