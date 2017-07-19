package com.whatscover.service.mapper;

import com.whatscover.domain.*;
import com.whatscover.service.dto.InsuranceProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InsuranceProduct and its DTO InsuranceProductDTO.
 */
@Mapper(componentModel = "spring", uses = {InsuranceCompanyMapper.class, })
public interface InsuranceProductMapper extends EntityMapper <InsuranceProductDTO, InsuranceProduct> {

    @Mappings({
        @Mapping(source = "insuranceCompany.id", target = "insuranceCompanyId"),
        @Mapping(source = "insuranceCompany.name", target = "insuranceCompanyName")
    })
    InsuranceProductDTO toDto(InsuranceProduct insuranceProduct); 

    @Mapping(source = "insuranceCompanyId", target = "insuranceCompany")
    InsuranceProduct toEntity(InsuranceProductDTO insuranceProductDTO); 
    default InsuranceProduct fromId(Long id) {
        if (id == null) {
            return null;
        }
        InsuranceProduct insuranceProduct = new InsuranceProduct();
        insuranceProduct.setId(id);
        return insuranceProduct;
    }
}
