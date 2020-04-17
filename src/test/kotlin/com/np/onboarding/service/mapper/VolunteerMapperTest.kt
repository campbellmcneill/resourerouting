package com.np.onboarding.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VolunteerMapperTest {

    private lateinit var volunteerMapper: VolunteerMapper

    @BeforeEach
    fun setUp() {
        volunteerMapper = VolunteerMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = 1L
        assertThat(volunteerMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(volunteerMapper.fromId(null)).isNull()
    }
}
