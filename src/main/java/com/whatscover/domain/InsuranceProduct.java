package com.whatscover.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.whatscover.domain.enumeration.Gender;

/**
 * A InsuranceProduct.
 */
@Entity
@Table(name = "insurance_product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "insuranceproduct")
public class InsuranceProduct extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "code", length = 100, nullable = false)
    private String code;

    @NotNull
    @Size(max = 200)
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    
    @Size(max = 250)
    @Column(name = "short_description", length = 250)
    private String shortDescription;

    @Size(max = 500)
    @Column(name = "long_description", length = 500)
    private String longDescription;

    @Column(name = "min_entry_age_last_bday")
    private Integer minEntryAgeLastBday;

    @Column(name = "max_entry_age_last_bday")
    private Integer maxEntryAgeLastBday;

    @Column(name = "min_sum_assured")
    private Double minSumAssured;

    @Column(name = "max_sum_assured")
    private Double maxSumAssured;

    @Column(name = "prem_unit")
    private Double premUnit;

    @Column(name = "prod_weight_life")
    private Double prodWeightLife;

    @Column(name = "prod_weight_medical")
    private Double prodWeightMedical;

    @Column(name = "product_weight_pa")
    private Double productWeightPA;

    @Column(name = "product_weight_hosp_income")
    private Double productWeightHospIncome;

    @Column(name = "product_weight_ci")
    private Double productWeightCI;

    @Column(name = "product_weight_cancer")
    private Double productWeightCancer;

    @ManyToOne(optional = false)
    @NotNull
    private InsuranceCompany insuranceCompany;
    
    @ManyToOne(optional = false)
    @NotNull
    private ProductCategory productCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public InsuranceProduct code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public InsuranceProduct name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public InsuranceProduct gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getProductWeightPA() {
        return productWeightPA;
    }

    public InsuranceProduct productWeightPA(Double productWeightPA) {
        this.productWeightPA = productWeightPA;
        return this;
    }

    public void setProductWeightPA(Double productWeightPA) {
        this.productWeightPA = productWeightPA;
    }

    public Double getProductWeightHospIncome() {
        return productWeightHospIncome;
    }

    public InsuranceProduct productWeightHospIncome(Double productWeightHospIncome) {
        this.productWeightHospIncome = productWeightHospIncome;
        return this;
    }

    public void setProductWeightHospIncome(Double productWeightHospIncome) {
        this.productWeightHospIncome = productWeightHospIncome;
    }

    public Double getProductWeightCI() {
        return productWeightCI;
    }

    public InsuranceProduct productWeightCI(Double productWeightCI) {
        this.productWeightCI = productWeightCI;
        return this;
    }

    public void setProductWeightCI(Double productWeightCI) {
        this.productWeightCI = productWeightCI;
    }

    public Double getProductWeightCancer() {
        return productWeightCancer;
    }

    public InsuranceProduct productWeightCancer(Double productWeightCancer) {
        this.productWeightCancer = productWeightCancer;
        return this;
    }

    public void setProductWeightCancer(Double productWeightCancer) {
        this.productWeightCancer = productWeightCancer;
    }
    
    public String getShortDescription() {
        return shortDescription;
    }

    public InsuranceProduct shortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public InsuranceProduct longDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Integer getMinEntryAgeLastBday() {
        return minEntryAgeLastBday;
    }

    public InsuranceProduct minEntryAgeLastBday(Integer minEntryAgeLastBday) {
        this.minEntryAgeLastBday = minEntryAgeLastBday;
        return this;
    }

    public void setMinEntryAgeLastBday(Integer minEntryAgeLastBday) {
        this.minEntryAgeLastBday = minEntryAgeLastBday;
    }

    public Integer getMaxEntryAgeLastBday() {
        return maxEntryAgeLastBday;
    }

    public InsuranceProduct maxEntryAgeLastBday(Integer maxEntryAgeLastBday) {
        this.maxEntryAgeLastBday = maxEntryAgeLastBday;
        return this;
    }

    public void setMaxEntryAgeLastBday(Integer maxEntryAgeLastBday) {
        this.maxEntryAgeLastBday = maxEntryAgeLastBday;
    }

    public Double getMinSumAssured() {
        return minSumAssured;
    }

    public InsuranceProduct minSumAssured(Double minSumAssured) {
        this.minSumAssured = minSumAssured;
        return this;
    }

    public void setMinSumAssured(Double minSumAssured) {
        this.minSumAssured = minSumAssured;
    }

    public Double getMaxSumAssured() {
        return maxSumAssured;
    }

    public InsuranceProduct maxSumAssured(Double maxSumAssured) {
        this.maxSumAssured = maxSumAssured;
        return this;
    }

    public void setMaxSumAssured(Double maxSumAssured) {
        this.maxSumAssured = maxSumAssured;
    }

    public Double getPremUnit() {
        return premUnit;
    }

    public InsuranceProduct premUnit(Double premUnit) {
        this.premUnit = premUnit;
        return this;
    }

    public void setPremUnit(Double premUnit) {
        this.premUnit = premUnit;
    }

    public Double getProdWeightLife() {
        return prodWeightLife;
    }

    public InsuranceProduct prodWeightLife(Double prodWeightLife) {
        this.prodWeightLife = prodWeightLife;
        return this;
    }

    public void setProdWeightLife(Double prodWeightLife) {
        this.prodWeightLife = prodWeightLife;
    }

    public Double getProdWeightMedical() {
        return prodWeightMedical;
    }

    public InsuranceProduct prodWeightMedical(Double prodWeightMedical) {
        this.prodWeightMedical = prodWeightMedical;
        return this;
    }

    public void setProdWeightMedical(Double prodWeightMedical) {
        this.prodWeightMedical = prodWeightMedical;
    }

    public InsuranceCompany getInsuranceCompany() {
        return insuranceCompany;
    }

    public InsuranceProduct insuranceCompany(InsuranceCompany insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
        return this;
    }

    public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }
    
    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public InsuranceProduct productCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
        return this;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InsuranceProduct insuranceProduct = (InsuranceProduct) o;
        if (insuranceProduct.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), insuranceProduct.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InsuranceProduct{" +
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
