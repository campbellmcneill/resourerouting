package com.np.onboarding.service.dto

import com.np.onboarding.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CountryDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(CountryDTO::class)
        val countryDTO1 = CountryDTO()
        countryDTO1.id = 1L
        val countryDTO2 = CountryDTO()
        assertThat(countryDTO1).isNotEqualTo(countryDTO2)
        countryDTO2.id = countryDTO1.id
        assertThat(countryDTO1).isEqualTo(countryDTO2)
        countryDTO2.id = 2L
        assertThat(countryDTO1).isNotEqualTo(countryDTO2)
        countryDTO1.id = null
        assertThat(countryDTO1).isNotEqualTo(countryDTO2)
    }
}
