package com.np.onboarding.service.mapper

import com.np.onboarding.domain.Location
import com.np.onboarding.service.dto.LocationDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [Location] and its DTO [LocationDTO].
 */
@Mapper(componentModel = "spring", uses = [CountryMapper::class])
interface LocationMapper :
    EntityMapper<LocationDTO, Location> {

    @Mappings(
        Mapping(source = "country.id", target = "countryId")
    )
    override fun toDto(location: Location): LocationDTO

    @Mappings(
        Mapping(source = "countryId", target = "country")
    )
    override fun toEntity(locationDTO: LocationDTO): Location

    @JvmDefault
    fun fromId(id: Long?) = id?.let {
        val location = Location()
        location.id = id
        location
    }
}
