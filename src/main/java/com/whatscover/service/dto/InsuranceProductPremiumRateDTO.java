package com.whatscover.service.dto;


import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.whatscover.domain.enumeration.ProductPremiumRateEntityStatus;

/**
 * A DTO for the InsuranceProductPremiumRate entity.
 */
public class InsuranceProductPremiumRateDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private Integer entryAge;

    @NotNull
    private Double malePremiumRate;

    @NotNull
    private Double femalePremiumRate;

    @Size(max = 5)
    private String plan;

    private Long insuranceProductId;
    
    @NotNull
    private ProductPremiumRateEntityStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEntryAge() {
        return entryAge;
    }

    public void setEntryAge(Integer entryAge) {
        this.entryAge = entryAge;
    }

    public Double getMalePremiumRate() {
        return malePremiumRate;
    }

    public void setMalePremiumRate(Double malePremiumRate) {
        this.malePremiumRate = malePremiumRate;
    }

    public Double getFemalePremiumRate() {
        return femalePremiumRate;
    }

    public void setFemalePremiumRate(Double femalePremiumRate) {
        this.femalePremiumRate = femalePremiumRate;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Long getInsuranceProductId() {
        return insuranceProductId;
    }

    public void setInsuranceProductId(Long insuranceProductId) {
        this.insuranceProductId = insuranceProductId;
    }
    
    public ProductPremiumRateEntityStatus getStatus() {
 		return status;
 	}
 
	public void setStatus(ProductPremiumRateEntityStatus status) {
 		this.status = status;
 	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InsuranceProductPremiumRateDTO insuranceProductPremiumRateDTO = (InsuranceProductPremiumRateDTO) o;
        if(insuranceProductPremiumRateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), insuranceProductPremiumRateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InsuranceProductPremiumRateDTO{" +
            "id=" + getId() +
            ", entryAge='" + getEntryAge() + "'" +
            ", malePremiumRate='" + getMalePremiumRate() + "'" +
            ", femalePremiumRate='" + getFemalePremiumRate() + "'" +
            ", plan='" + getPlan() + "'" +
            "}";
    }
}
