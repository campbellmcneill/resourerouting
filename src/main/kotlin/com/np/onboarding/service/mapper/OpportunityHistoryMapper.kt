package com.np.onboarding.service.mapper

import com.np.onboarding.domain.OpportunityHistory
import com.np.onboarding.service.dto.OpportunityHistoryDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [OpportunityHistory] and its DTO [OpportunityHistoryDTO].
 */
@Mapper(componentModel = "spring", uses = [OpportunityMapper::class, OrganizationMapper::class, VolunteerMapper::class])
interface OpportunityHistoryMapper :
    EntityMapper<OpportunityHistoryDTO, OpportunityHistory> {

    @Mappings(
        Mapping(source = "opportunity.id", target = "opportunityId"),
        Mapping(source = "organization.id", target = "organizationId"),
        Mapping(source = "volunteer.id", target = "volunteerId")
    )
    override fun toDto(opportunityHistory: OpportunityHistory): OpportunityHistoryDTO

    @Mappings(
        Mapping(source = "opportunityId", target = "opportunity"),
        Mapping(source = "organizationId", target = "organization"),
        Mapping(source = "volunteerId", target = "volunteer")
    )
    override fun toEntity(opportunityHistoryDTO: OpportunityHistoryDTO): OpportunityHistory

    @JvmDefault
    fun fromId(id: Long?) = id?.let {
        val opportunityHistory = OpportunityHistory()
        opportunityHistory.id = id
        opportunityHistory
    }
}
