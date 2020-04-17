package com.np.onboarding.service.mapper

import com.np.onboarding.domain.Opportunity
import com.np.onboarding.service.dto.OpportunityDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [Opportunity] and its DTO [OpportunityDTO].
 */
@Mapper(componentModel = "spring", uses = [TaskMapper::class, VolunteerMapper::class])
interface OpportunityMapper :
    EntityMapper<OpportunityDTO, Opportunity> {

    @Mappings(
        Mapping(source = "volunteer.id", target = "volunteerId")
    )
    override fun toDto(opportunity: Opportunity): OpportunityDTO

    @Mappings(
        Mapping(target = "removeTask", ignore = true),
        Mapping(source = "volunteerId", target = "volunteer")
    )
    override fun toEntity(opportunityDTO: OpportunityDTO): Opportunity

    @JvmDefault
    fun fromId(id: Long?) = id?.let {
        val opportunity = Opportunity()
        opportunity.id = id
        opportunity
    }
}
