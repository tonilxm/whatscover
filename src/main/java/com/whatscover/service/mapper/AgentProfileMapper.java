package com.whatscover.service.mapper;

import com.whatscover.domain.*;
import com.whatscover.service.dto.AgentProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AgentProfile and its DTO AgentProfileDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, InsuranceCompanyMapper.class, InsuranceAgencyMapper.class, })
public interface AgentProfileMapper extends EntityMapper <AgentProfileDTO, AgentProfile> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")

    @Mapping(source = "insuranceCompany.id", target = "insuranceCompanyId")
    @Mapping(source = "insuranceCompany.name", target = "insuranceCompanyName")

    @Mapping(source = "insuranceAgency.id", target = "insuranceAgencyId")
    @Mapping(source = "insuranceAgency.name", target = "insuranceAgencyName")
    AgentProfileDTO toDto(AgentProfile agentProfile); 

    @Mapping(source = "userId", target = "user")

    @Mapping(source = "insuranceCompanyId", target = "insuranceCompany")
    @Mapping(source = "insuranceAgencyId", target = "insuranceAgency")
    
    AgentProfile toEntity(AgentProfileDTO agentProfileDTO); 
    default AgentProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        AgentProfile agentProfile = new AgentProfile();
        agentProfile.setId(id);
        return agentProfile;
    }
}
