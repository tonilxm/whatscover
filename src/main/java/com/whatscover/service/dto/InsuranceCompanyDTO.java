package com.whatscover.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the InsuranceCompany entity.
 */
public class InsuranceCompanyDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String code;

    @Size(max = 100)
    private String name;

    @Size(max = 200)
    private String description;

    @Size(max = 200)
    private String address_1;

    @Size(max = 200)
    private String address_2;

    @Size(max = 200)
    private String address_3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getAddress_3() {
        return address_3;
    }

    public void setAddress_3(String address_3) {
        this.address_3 = address_3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InsuranceCompanyDTO insuranceCompanyDTO = (InsuranceCompanyDTO) o;
        if(insuranceCompanyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), insuranceCompanyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InsuranceCompanyDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", address_1='" + getAddress_1() + "'" +
            ", address_2='" + getAddress_2() + "'" +
            ", address_3='" + getAddress_3() + "'" +
            "}";
    }
}
