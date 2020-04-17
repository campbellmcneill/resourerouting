package com.np.onboarding.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RegionMapperTest {

    private lateinit var regionMapper: RegionMapper

    @BeforeEach
    fun setUp() {
        regionMapper = RegionMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = 1L
        assertThat(regionMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(regionMapper.fromId(null)).isNull()
    }
}
