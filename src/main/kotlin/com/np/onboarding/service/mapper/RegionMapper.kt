package com.np.onboarding.service.mapper

import com.np.onboarding.domain.Region
import com.np.onboarding.service.dto.RegionDTO
import org.mapstruct.Mapper

/**
 * Mapper for the entity [Region] and its DTO [RegionDTO].
 */
@Mapper(componentModel = "spring", uses = [])
interface RegionMapper :
    EntityMapper<RegionDTO, Region> {

    override fun toEntity(regionDTO: RegionDTO): Region

    @JvmDefault
    fun fromId(id: Long?) = id?.let {
        val region = Region()
        region.id = id
        region
    }
}
