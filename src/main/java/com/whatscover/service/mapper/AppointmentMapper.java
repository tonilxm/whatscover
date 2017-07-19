package com.whatscover.service.mapper;

import com.whatscover.domain.Appointment;
import com.whatscover.service.dto.AppointmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Appointment and its DTO AppointmentDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerProfileMapper.class, AgentProfileMapper.class, })
public interface AppointmentMapper extends EntityMapper <AppointmentDTO, Appointment> {

    @Mapping(source = "customerProfile.id", target = "customerProfileId")

    @Mapping(source = "assignedAgentProfile.id", target = "assignedAgentProfileId")
    AppointmentDTO toDto(Appointment appointment);

    @Mapping(source = "customerProfileId", target = "customerProfile")

    @Mapping(source = "assignedAgentProfileId", target = "assignedAgentProfile")
    Appointment toEntity(AppointmentDTO appointmentDTO);
    default Appointment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Appointment appointment = new Appointment();
        appointment.setId(id);
        return appointment;
    }
}
