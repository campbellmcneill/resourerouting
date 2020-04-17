package com.np.onboarding.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OrganizationMapperTest {

    private lateinit var organizationMapper: OrganizationMapper

    @BeforeEach
    fun setUp() {
        organizationMapper = OrganizationMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = 1L
        assertThat(organizationMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(organizationMapper.fromId(null)).isNull()
    }
}
