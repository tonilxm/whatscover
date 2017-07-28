package com.whatscover.service.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
            "}";
    }
}
