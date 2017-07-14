package com.whatscover.service.mapper;

import com.whatscover.domain.*;
import com.whatscover.service.dto.InsuranceCompanyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InsuranceCompany and its DTO InsuranceCompanyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InsuranceCompanyMapper extends EntityMapper <InsuranceCompanyDTO, InsuranceCompany> {
    
    
    default InsuranceCompany fromId(Long id) {
        if (id == null) {
            return null;
        }
        InsuranceCompany insuranceCompany = new InsuranceCompany();
        insuranceCompany.setId(id);
        return insuranceCompany;
    }
}
