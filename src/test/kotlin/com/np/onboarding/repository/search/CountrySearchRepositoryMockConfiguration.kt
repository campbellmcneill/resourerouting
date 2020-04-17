package com.np.onboarding.repository.search

import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration

/**
 * Configure a Mock version of CountrySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
class CountrySearchRepositoryMockConfiguration {

    @MockBean
    private lateinit var mockCountrySearchRepository: CountrySearchRepository
}
