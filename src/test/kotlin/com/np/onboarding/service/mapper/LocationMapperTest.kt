package com.np.onboarding.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocationMapperTest {

    private lateinit var locationMapper: LocationMapper

    @BeforeEach
    fun setUp() {
        locationMapper = LocationMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = 1L
        assertThat(locationMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(locationMapper.fromId(null)).isNull()
    }
}
