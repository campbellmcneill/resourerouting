package com.np.onboarding.service.mapper

import com.np.onboarding.domain.Country
import com.np.onboarding.service.dto.CountryDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [Country] and its DTO [CountryDTO].
 */
@Mapper(componentModel = "spring", uses = [RegionMapper::class])
interface CountryMapper :
    EntityMapper<CountryDTO, Country> {

    @Mappings(
        Mapping(source = "region.id", target = "regionId")
    )
    override fun toDto(country: Country): CountryDTO

    @Mappings(
        Mapping(source = "regionId", target = "region")
    )
    override fun toEntity(countryDTO: CountryDTO): Country

    @JvmDefault
    fun fromId(id: Long?) = id?.let {
        val country = Country()
        country.id = id
        country
    }
}
