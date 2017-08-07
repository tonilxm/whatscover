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
    
    @Size(max = 250)
    private String shortDescription;

    @Size(max = 500)
    private String longDescription;

    private Integer minEntryAgeLastBday;

    private Integer maxEntryAgeLastBday;

    private Double minSumAssured;

    private Double maxSumAssured;

    private Double premUnit;

    private Double prodWeightLife;

    private Double prodWeightMedical;

    private Gender gender;

    private Double productWeightPA;

    private Double productWeightHospIncome;

    private Double productWeightCI;

    private Double productWeightCancer;

    private Long insuranceCompanyId;
    
    @Size(max = 100)
    private String insuranceCompanyName;
    
    private Long productCategoryId;
    
    @Size(max = 100)
    private String productCategoryName;

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public Double getProductWeightCancer() {
        return productWeightCancer;
    }

    public void setProductWeightCancer(Double productWeightCancer) {
        this.productWeightCancer = productWeightCancer;
    }
    
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Integer getMinEntryAgeLastBday() {
        return minEntryAgeLastBday;
    }

    public void setMinEntryAgeLastBday(Integer minEntryAgeLastBday) {
        this.minEntryAgeLastBday = minEntryAgeLastBday;
    }

    public Integer getMaxEntryAgeLastBday() {
        return maxEntryAgeLastBday;
    }

    public void setMaxEntryAgeLastBday(Integer maxEntryAgeLastBday) {
        this.maxEntryAgeLastBday = maxEntryAgeLastBday;
    }

    public Double getMinSumAssured() {
        return minSumAssured;
    }

    public void setMinSumAssured(Double minSumAssured) {
        this.minSumAssured = minSumAssured;
    }

    public Double getMaxSumAssured() {
        return maxSumAssured;
    }

    public void setMaxSumAssured(Double maxSumAssured) {
        this.maxSumAssured = maxSumAssured;
    }

    public Double getPremUnit() {
        return premUnit;
    }

    public void setPremUnit(Double premUnit) {
        this.premUnit = premUnit;
    }

    public Double getProdWeightLife() {
        return prodWeightLife;
    }

    public void setProdWeightLife(Double prodWeightLife) {
        this.prodWeightLife = prodWeightLife;
    }

    public Double getProdWeightMedical() {
        return prodWeightMedical;
    }

    public void setProdWeightMedical(Double prodWeightMedical) {
        this.prodWeightMedical = prodWeightMedical;
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

	public Long getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
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
            ", gender='" + getGender() + "'" +
            ", productWeightPA='" + getProductWeightPA() + "'" +
            ", productWeightHospIncome='" + getProductWeightHospIncome() + "'" +
            ", productWeightCI='" + getProductWeightCI() + "'" +
            ", productWeightCancer='" + getProductWeightCancer() + "'" +
            ", shortDescription='" + getShortDescription() + "'" +
            ", longDescription='" + getLongDescription() + "'" +
            ", minEntryAgeLastBday='" + getMinEntryAgeLastBday() + "'" +
            ", maxEntryAgeLastBday='" + getMaxEntryAgeLastBday() + "'" +
            ", minSumAssured='" + getMinSumAssured() + "'" +
            ", maxSumAssured='" + getMaxSumAssured() + "'" +
            ", premUnit='" + getPremUnit() + "'" +
            ", prodWeightLife='" + getProdWeightLife() + "'" +
            ", prodWeightMedical='" + getProdWeightMedical() + "'" +
            "}";
    }
}
