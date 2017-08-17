package com.whatscover.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.whatscover.domain.enumeration.Gender;

/**
 * A DTO for the AgentProfile entity.
 */
public class AgentProfileDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String agent_code;

    @NotNull
    @Size(max = 50)
    private String first_name;

    @Size(max = 50)
    private String middle_name;

    @Size(max = 50)
    private String last_name;

    private Gender gender;

    @NotNull
    @Size(max = 100)
    private String email;

    private LocalDate dob;

    @Size(max = 500)
    private String address;

    @Size(max = 500)
    private String photo_dir;

    @Size(max = 100)
    private String phone;

    private Long userId;

    private String userLogin;

    private Long insuranceCompanyId;

    @Size(max = 100)
    private String insuranceCompanyName;

    private Long insuranceAgencyId;

    @Size(max = 100)
    private String insuranceAgencyName;

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
        this.first_name = changeNull2EmptyVal(first_name);
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = changeNull2EmptyVal(middle_name);
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = changeNull2EmptyVal(last_name);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto_dir() {
        return photo_dir;
    }

    public void setPhoto_dir(String photo_dir) {
        this.photo_dir = photo_dir;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Long getInsuranceAgencyId() {
        return insuranceAgencyId;
    }

    public String getInsuranceCompanyName() {
		return insuranceCompanyName;
	}

	public void setInsuranceCompanyName(String insuranceCompanyName) {
		this.insuranceCompanyName = insuranceCompanyName;
	}

	public void setInsuranceAgencyId(Long insuranceAgencyId) {
        this.insuranceAgencyId = insuranceAgencyId;
    }

	public String getInsuranceAgencyName() {
		return insuranceAgencyName;
	}

	public void setInsuranceAgencyName(String insuranceAgencyName) {
		this.insuranceAgencyName = insuranceAgencyName;
	}
	
	private String changeNull2EmptyVal(String value) {
		
		if (value == null) {
			return "";
		} else {
			return value;
		}
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
            ", address='" + getAddress() + "'" +
            ", photo_dir='" + getPhoto_dir() + "'" +
            ", phone='" + getPhone() + "'" +
            ", insuranceCompanyId='" + getInsuranceCompanyId() + "'" +
            ", insuranceAgencyId='" + getInsuranceAgencyId() + "'" +            
            "}";
    }
}
