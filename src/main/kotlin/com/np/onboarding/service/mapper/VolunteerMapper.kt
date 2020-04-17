package com.np.onboarding.service.mapper

import com.np.onboarding.domain.Volunteer
import com.np.onboarding.service.dto.VolunteerDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [Volunteer] and its DTO [VolunteerDTO].
 */
@Mapper(componentModel = "spring", uses = [OrganizationMapper::class])
interface VolunteerMapper :
    EntityMapper<VolunteerDTO, Volunteer> {

    @Mappings(
        Mapping(source = "supervisor.id", target = "supervisorId"),
        Mapping(source = "organization.id", target = "organizationId")
    )
    override fun toDto(volunteer: Volunteer): VolunteerDTO

    @Mappings(
        Mapping(target = "opportunities", ignore = true),
        Mapping(target = "removeOpportunity", ignore = true),
        Mapping(source = "supervisorId", target = "supervisor"),
        Mapping(source = "organizationId", target = "organization")
    )
    override fun toEntity(volunteerDTO: VolunteerDTO): Volunteer

    @JvmDefault
    fun fromId(id: Long?) = id?.let {
        val volunteer = Volunteer()
        volunteer.id = id
        volunteer
    }
}
