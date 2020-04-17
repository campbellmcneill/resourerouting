package com.np.onboarding.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OpportunityHistoryMapperTest {

    private lateinit var opportunityHistoryMapper: OpportunityHistoryMapper

    @BeforeEach
    fun setUp() {
        opportunityHistoryMapper = OpportunityHistoryMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = 1L
        assertThat(opportunityHistoryMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(opportunityHistoryMapper.fromId(null)).isNull()
    }
}
