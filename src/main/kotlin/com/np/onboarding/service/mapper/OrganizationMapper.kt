package com.np.onboarding.service.mapper

import com.np.onboarding.domain.Organization
import com.np.onboarding.service.dto.OrganizationDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [Organization] and its DTO [OrganizationDTO].
 */
@Mapper(componentModel = "spring", uses = [LocationMapper::class, OpportunityMapper::class])
interface OrganizationMapper :
    EntityMapper<OrganizationDTO, Organization> {

    @Mappings(
        Mapping(source = "location.id", target = "locationId"),
        Mapping(source = "opportunity.id", target = "opportunityId")
    )
    override fun toDto(organization: Organization): OrganizationDTO

    @Mappings(
        Mapping(source = "locationId", target = "location"),
        Mapping(source = "opportunityId", target = "opportunity"),
        Mapping(target = "volunteers", ignore = true),
        Mapping(target = "removeVolunteer", ignore = true)
    )
    override fun toEntity(organizationDTO: OrganizationDTO): Organization

    @JvmDefault
    fun fromId(id: Long?) = id?.let {
        val organization = Organization()
        organization.id = id
        organization
    }
}
