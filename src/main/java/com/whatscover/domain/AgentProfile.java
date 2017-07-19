package com.whatscover.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.whatscover.config.Constants;
import com.whatscover.domain.enumeration.Gender;

/**
 * A AgentProfile.
 */
@Entity
@Table(name = "agent_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "agentprofile")
public class AgentProfile extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "agent_code", length = 100, nullable = false)
    private String agent_code;

    @Size(max = 100)
    @Column(name = Constants.AGENT_PROFILE_FIRST_NAME, length = 100)
    private String first_name;

    @Size(max = 100)
    @Column(name = Constants.AGENT_PROFILE_MIDDLE_NAME, length = 100)
    private String middle_name;

    @Size(max = 100)
    @Column(name = Constants.AGENT_PROFILE_LAST_NAME, length = 100)
    private String last_name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @NotNull
    @Size(max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "dob")
    private LocalDate dob;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    private InsuranceCompany insuranceCompany;

    @ManyToOne
    private InsuranceAgency insuranceAgency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgent_code() {
        return agent_code;
    }

    public AgentProfile agent_code(String agent_code) {
        this.agent_code = agent_code;
        return this;
    }

    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }

    public String getFirst_name() {
        return first_name;
    }

    public AgentProfile first_name(String first_name) {
        this.first_name = first_name;
        return this;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public AgentProfile middle_name(String middle_name) {
        this.middle_name = middle_name;
        return this;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public AgentProfile last_name(String last_name) {
        this.last_name = last_name;
        return this;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Gender getGender() {
        return gender;
    }

    public AgentProfile gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public AgentProfile email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public AgentProfile dob(LocalDate dob) {
        this.dob = dob;
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public User getUser() {
        return user;
    }

    public AgentProfile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InsuranceCompany getInsuranceCompany() {
        return insuranceCompany;
    }

    public AgentProfile insuranceCompany(InsuranceCompany insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
        return this;
    }

    public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public InsuranceAgency getInsuranceAgency() {
        return insuranceAgency;
    }

    public AgentProfile insuranceAgency(InsuranceAgency insuranceAgency) {
        this.insuranceAgency = insuranceAgency;
        return this;
    }

    public void setInsuranceAgency(InsuranceAgency insuranceAgency) {
        this.insuranceAgency = insuranceAgency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentProfile agentProfile = (AgentProfile) o;
        if (agentProfile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), agentProfile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AgentProfile{" +
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
