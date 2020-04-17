package com.np.onboarding.service.dto

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OpportunityDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(OpportunityDTO::class)
        val opportunityDTO1 = OpportunityDTO()
        opportunityDTO1.id = 1L
        val opportunityDTO2 = OpportunityDTO()
        assertThat(opportunityDTO1).isNotEqualTo(opportunityDTO2)
        opportunityDTO2.id = opportunityDTO1.id
        assertThat(opportunityDTO1).isEqualTo(opportunityDTO2)
        opportunityDTO2.id = 2L
        assertThat(opportunityDTO1).isNotEqualTo(opportunityDTO2)
        opportunityDTO1.id = null
        assertThat(opportunityDTO1).isNotEqualTo(opportunityDTO2)
    }
}
