package com.whatscover.service.mapper;

import com.whatscover.domain.*;
import com.whatscover.service.dto.InsuranceProductPremiumRateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InsuranceProductPremiumRate and its DTO InsuranceProductPremiumRateDTO.
 */
@Mapper(componentModel = "spring", uses = {InsuranceProductMapper.class, })
public interface InsuranceProductPremiumRateMapper extends EntityMapper <InsuranceProductPremiumRateDTO, InsuranceProductPremiumRate> {

    
    @Mappings({
    	@Mapping(source = "insuranceProduct.id", target = "insuranceProductId"),
        @Mapping(target = "status", expression = "java(com.whatscover.domain.enumeration.ProductPremiumRateEntityStatus.DEFAULT)")
    })
    InsuranceProductPremiumRateDTO toDto(InsuranceProductPremiumRate insuranceProductPremiumRate); 

    @Mapping(source = "insuranceProductId", target = "insuranceProduct")
    InsuranceProductPremiumRate toEntity(InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO); 
    default InsuranceProductPremiumRate fromId(Long id) {
        if (id == null) {
            return null;
        }
        InsuranceProductPremiumRate insuranceProductPremiumRate = new InsuranceProductPremiumRate();
        insuranceProductPremiumRate.setId(id);
        return insuranceProductPremiumRate;
    }
    
}
