package com.np.onboarding.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountryMapperTest {

    private lateinit var countryMapper: CountryMapper

    @BeforeEach
    fun setUp() {
        countryMapper = CountryMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = 1L
        assertThat(countryMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(countryMapper.fromId(null)).isNull()
    }
}
