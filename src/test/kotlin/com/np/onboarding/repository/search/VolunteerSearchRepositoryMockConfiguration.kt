package com.np.onboarding.repository.search

import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration

/**
 * Configure a Mock version of VolunteerSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
class VolunteerSearchRepositoryMockConfiguration {

    @MockBean
    private lateinit var mockVolunteerSearchRepository: VolunteerSearchRepository
}
