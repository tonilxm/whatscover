package com.whatscover.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.whatscover.domain.enumeration.Gender;

/**
 * A DTO for the AgentProfile entity.
 */
public class AgentProfileDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String agent_code;

    @Size(max = 100)
    private String first_name;

    @Size(max = 100)
    private String middle_name;

    @Size(max = 100)
    private String last_name;

    private Gender gender;

    @NotNull
    @Size(max = 100)
    private String email;

    private LocalDate dob;

    private Long userId;

    private String userLogin;

    private Long insuranceCompanyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgent_code() {
        return agent_code;
    }

    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getInsuranceCompanyId() {
        return insuranceCompanyId;
    }

    public void setInsuranceCompanyId(Long insuranceCompanyId) {
        this.insuranceCompanyId = insuranceCompanyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AgentProfileDTO agentProfileDTO = (AgentProfileDTO) o;
        if(agentProfileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), agentProfileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AgentProfileDTO{" +
            "id=" + getId() +
            ", agent_code='" + getAgent_code() + "'" +
            ", first_name='" + getFirst_name() + "'" +
            ", middle_name='" + getMiddle_name() + "'" +
            ", last_name='" + getLast_name() + "'" +
            ", gender='" + getGender() + "'" +
            ", email='" + getEmail() + "'" +
            ", dob='" + getDob() + "'" +
            "}";
    }
}