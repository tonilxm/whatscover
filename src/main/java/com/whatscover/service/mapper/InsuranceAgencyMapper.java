package com.whatscover.service.mapper;

import com.whatscover.domain.*;
import com.whatscover.service.dto.InsuranceAgencyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InsuranceAgency and its DTO InsuranceAgencyDTO.
 */
@Mapper(componentModel = "spring", uses = {InsuranceCompanyMapper.class, })
public interface InsuranceAgencyMapper extends EntityMapper <InsuranceAgencyDTO, InsuranceAgency> {

    @Mapping(source = "insuranceCompany.id", target = "insuranceCompanyId")
    InsuranceAgencyDTO toDto(InsuranceAgency insuranceAgency); 

    @Mapping(source = "insuranceCompanyId", target = "insuranceCompany")
    InsuranceAgency toEntity(InsuranceAgencyDTO insuranceAgencyDTO); 
    default InsuranceAgency fromId(Long id) {
        if (id == null) {
            return null;
        }
        InsuranceAgency insuranceAgency = new InsuranceAgency();
        insuranceAgency.setId(id);
        return insuranceAgency;
    }
}
