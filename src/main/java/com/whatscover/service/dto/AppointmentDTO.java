package com.whatscover.service.dto;


import com.whatscover.domain.enumeration.AppointmentStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the Appointment entity.
 */
public class AppointmentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String phone;

    private Float locationGeoLong;

    private Float locationGeoLat;

    @NotNull
    @Size(max = 500)
    private String locationAddress;

    @NotNull
    private ZonedDateTime datetime;

    private Instant assignedDatetime;

    @NotNull
    private AppointmentStatus status;

    private Long customerProfileId;

    private Long assignedAgentProfileId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Float getLocationGeoLong() {
        return locationGeoLong;
    }

    public void setLocationGeoLong(Float locationGeoLong) {
        this.locationGeoLong = locationGeoLong;
    }

    public Float getLocationGeoLat() {
        return locationGeoLat;
    }

    public void setLocationGeoLat(Float locationGeoLat) {
        this.locationGeoLat = locationGeoLat;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public ZonedDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(ZonedDateTime datetime) {
        this.datetime = datetime;
    }

    public Instant getAssignedDatetime() {
        return assignedDatetime;
    }

    public void setAssignedDatetime(Instant assignedDatetime) {
        this.assignedDatetime = assignedDatetime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Long getCustomerProfileId() {
        return customerProfileId;
    }

    public void setCustomerProfileId(Long customerProfileId) {
        this.customerProfileId = customerProfileId;
    }

    public Long getAssignedAgentProfileId() {
        return assignedAgentProfileId;
    }

    public void setAssignedAgentProfileId(Long agentProfileId) {
        this.assignedAgentProfileId = agentProfileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AppointmentDTO appointmentDTO = (AppointmentDTO) o;
        if(appointmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appointmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AppointmentDTO{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            ", locationGeoLong='" + getLocationGeoLong() + "'" +
            ", locationGeoLat='" + getLocationGeoLat() + "'" +
            ", locationAddress='" + getLocationAddress() + "'" +
            ", datetime='" + getDatetime() + "'" +
            ", assignedDatetime='" + getAssignedDatetime() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
