package com.whatscover.service.mapper;

import com.whatscover.domain.CustomerProfile;
import com.whatscover.service.dto.CustomerProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity CustomerProfile and its DTO CustomerProfileDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface CustomerProfileMapper extends EntityMapper <CustomerProfileDTO, CustomerProfile> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    CustomerProfileDTO toDto(CustomerProfile customerProfile);

    @Mapping(source = "userId", target = "user")
    CustomerProfile toEntity(CustomerProfileDTO customerProfileDTO);
    default CustomerProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setId(id);
        return customerProfile;
    }
}
