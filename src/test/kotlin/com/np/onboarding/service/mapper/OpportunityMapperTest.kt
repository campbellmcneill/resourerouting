package com.np.onboarding.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OpportunityMapperTest {

    private lateinit var opportunityMapper: OpportunityMapper

    @BeforeEach
    fun setUp() {
        opportunityMapper = OpportunityMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = 1L
        assertThat(opportunityMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(opportunityMapper.fromId(null)).isNull()
    }
}
