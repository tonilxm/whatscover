package com.whatscover.service.dto;


import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.whatscover.domain.enumeration.Gender;

/**
 * A DTO for the InsuranceProduct entity.
 */
public class InsuranceProductDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String code;

    @NotNull
    @Size(max = 200)
    private String name;

    private Integer entryAgeLastBday;

    private Gender gender;

    private Integer premiumTerm;

    private Integer policyTerm;

    private Double premiumRate;

    private Double sumAssuredDeath;

    private Double sumAssuredTPD;

    private Double sumAssuredADD;

    private Double sumAssuredHospIncome;

    private Double sumAssuredCI;

    private Double sumAssuredMedic;

    private Double sumAssuredCancer;

    private Double productWeightDeath;

    private Double productWeightPA;

    private Double productWeightHospIncome;

    private Double productWeightCI;

    private Double productWeightMedic;

    private Double productWeightCancer;

    private Long insuranceCompanyId;
    
    @Size(max = 100)
    private String insuranceCompanyName;

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

    public Integer getEntryAgeLastBday() {
        return entryAgeLastBday;
    }

    public void setEntryAgeLastBday(Integer entryAgeLastBday) {
        this.entryAgeLastBday = entryAgeLastBday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getPremiumTerm() {
        return premiumTerm;
    }

    public void setPremiumTerm(Integer premiumTerm) {
        this.premiumTerm = premiumTerm;
    }

    public Integer getPolicyTerm() {
        return policyTerm;
    }

    public void setPolicyTerm(Integer policyTerm) {
        this.policyTerm = policyTerm;
    }

    public Double getPremiumRate() {
        return premiumRate;
    }

    public void setPremiumRate(Double premiumRate) {
        this.premiumRate = premiumRate;
    }

    public Double getSumAssuredDeath() {
        return sumAssuredDeath;
    }

    public void setSumAssuredDeath(Double sumAssuredDeath) {
        this.sumAssuredDeath = sumAssuredDeath;
    }

    public Double getSumAssuredTPD() {
        return sumAssuredTPD;
    }

    public void setSumAssuredTPD(Double sumAssuredTPD) {
        this.sumAssuredTPD = sumAssuredTPD;
    }

    public Double getSumAssuredADD() {
        return sumAssuredADD;
    }

    public void setSumAssuredADD(Double sumAssuredADD) {
        this.sumAssuredADD = sumAssuredADD;
    }

    public Double getSumAssuredHospIncome() {
        return sumAssuredHospIncome;
    }

    public void setSumAssuredHospIncome(Double sumAssuredHospIncome) {
        this.sumAssuredHospIncome = sumAssuredHospIncome;
    }

    public Double getSumAssuredCI() {
        return sumAssuredCI;
    }

    public void setSumAssuredCI(Double sumAssuredCI) {
        this.sumAssuredCI = sumAssuredCI;
    }

    public Double getSumAssuredMedic() {
        return sumAssuredMedic;
    }

    public void setSumAssuredMedic(Double sumAssuredMedic) {
        this.sumAssuredMedic = sumAssuredMedic;
    }

    public Double getSumAssuredCancer() {
        return sumAssuredCancer;
    }

    public void setSumAssuredCancer(Double sumAssuredCancer) {
        this.sumAssuredCancer = sumAssuredCancer;
    }

    public Double getProductWeightDeath() {
        return productWeightDeath;
    }

    public void setProductWeightDeath(Double productWeightDeath) {
        this.productWeightDeath = productWeightDeath;
    }

    public Double getProductWeightPA() {
        return productWeightPA;
    }

    public void setProductWeightPA(Double productWeightPA) {
        this.productWeightPA = productWeightPA;
    }

    public Double getProductWeightHospIncome() {
        return productWeightHospIncome;
    }

    public void setProductWeightHospIncome(Double productWeightHospIncome) {
        this.productWeightHospIncome = productWeightHospIncome;
    }

    public Double getProductWeightCI() {
        return productWeightCI;
    }

    public void setProductWeightCI(Double productWeightCI) {
        this.productWeightCI = productWeightCI;
    }

    public Double getProductWeightMedic() {
        return productWeightMedic;
    }

    public void setProductWeightMedic(Double productWeightMedic) {
        this.productWeightMedic = productWeightMedic;
    }

    public Double getProductWeightCancer() {
        return productWeightCancer;
    }

    public void setProductWeightCancer(Double productWeightCancer) {
        this.productWeightCancer = productWeightCancer;
    }

    public Long getInsuranceCompanyId() {
        return insuranceCompanyId;
    }

    public void setInsuranceCompanyId(Long insuranceCompanyId) {
        this.insuranceCompanyId = insuranceCompanyId;
    }
    
    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InsuranceProductDTO insuranceProductDTO = (InsuranceProductDTO) o;
        if(insuranceProductDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), insuranceProductDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InsuranceProductDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", entryAgeLastBday='" + getEntryAgeLastBday() + "'" +
            ", gender='" + getGender() + "'" +
            ", premiumTerm='" + getPremiumTerm() + "'" +
            ", policyTerm='" + getPolicyTerm() + "'" +
            ", premiumRate='" + getPremiumRate() + "'" +
            ", sumAssuredDeath='" + getSumAssuredDeath() + "'" +
            ", sumAssuredTPD='" + getSumAssuredTPD() + "'" +
            ", sumAssuredADD='" + getSumAssuredADD() + "'" +
            ", sumAssuredHospIncome='" + getSumAssuredHospIncome() + "'" +
            ", sumAssuredCI='" + getSumAssuredCI() + "'" +
            ", sumAssuredMedic='" + getSumAssuredMedic() + "'" +
            ", sumAssuredCancer='" + getSumAssuredCancer() + "'" +
            ", productWeightDeath='" + getProductWeightDeath() + "'" +
            ", productWeightPA='" + getProductWeightPA() + "'" +
            ", productWeightHospIncome='" + getProductWeightHospIncome() + "'" +
            ", productWeightCI='" + getProductWeightCI() + "'" +
            ", productWeightMedic='" + getProductWeightMedic() + "'" +
            ", productWeightCancer='" + getProductWeightCancer() + "'" +
            "}";
    }
}
