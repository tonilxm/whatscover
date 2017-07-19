package com.whatscover.domain;

import com.whatscover.domain.enumeration.AppointmentStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Appointment.
 */
@Entity
@Table(name = "appointment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "appointment")
public class Appointment extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "phone", length = 50, nullable = false)
    private String phone;

    @Column(name = "location_geo_long")
    private Float locationGeoLong;

    @Column(name = "location_geo_lat")
    private Float locationGeoLat;

    @NotNull
    @Size(max = 500)
    @Column(name = "location_address", length = 500, nullable = false)
    private String locationAddress;

    @NotNull
    @Column(name = "datetime", nullable = false)
    private ZonedDateTime datetime;

    @Column(name = "assigned_datetime")
    private Instant assignedDatetime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @ManyToOne(optional = false)
    @NotNull
    private CustomerProfile customerProfile;

    @ManyToOne
    private AgentProfile assignedAgentProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public Appointment phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Float getLocationGeoLong() {
        return locationGeoLong;
    }

    public Appointment locationGeoLong(Float locationGeoLong) {
        this.locationGeoLong = locationGeoLong;
        return this;
    }

    public void setLocationGeoLong(Float locationGeoLong) {
        this.locationGeoLong = locationGeoLong;
    }

    public Float getLocationGeoLat() {
        return locationGeoLat;
    }

    public Appointment locationGeoLat(Float locationGeoLat) {
        this.locationGeoLat = locationGeoLat;
        return this;
    }

    public void setLocationGeoLat(Float locationGeoLat) {
        this.locationGeoLat = locationGeoLat;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public Appointment locationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
        return this;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public ZonedDateTime getDatetime() {
        return datetime;
    }

    public Appointment datetime(ZonedDateTime datetime) {
        this.datetime = datetime;
        return this;
    }

    public void setDatetime(ZonedDateTime datetime) {
        this.datetime = datetime;
    }

    public Instant getAssignedDatetime() {
        return assignedDatetime;
    }

    public Appointment assignedDatetime(Instant assignedDatetime) {
        this.assignedDatetime = assignedDatetime;
        return this;
    }

    public void setAssignedDatetime(Instant assignedDatetime) {
        this.assignedDatetime = assignedDatetime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public Appointment status(AppointmentStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public CustomerProfile getCustomerProfile() {
        return customerProfile;
    }

    public Appointment customerProfile(CustomerProfile customerProfile) {
        this.customerProfile = customerProfile;
        return this;
    }

    public void setCustomerProfile(CustomerProfile customerProfile) {
        this.customerProfile = customerProfile;
    }

    public AgentProfile getAssignedAgentProfile() {
        return assignedAgentProfile;
    }

    public Appointment assignedAgentProfile(AgentProfile agentProfile) {
        this.assignedAgentProfile = agentProfile;
        return this;
    }

    public void setAssignedAgentProfile(AgentProfile agentProfile) {
        this.assignedAgentProfile = agentProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Appointment appointment = (Appointment) o;
        if (appointment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appointment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Appointment{" +
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
