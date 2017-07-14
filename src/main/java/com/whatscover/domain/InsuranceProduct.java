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

    @Column(name = "entry_age_last_bday")
    private Integer entryAgeLastBday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "premium_term")
    private Integer premiumTerm;

    @Column(name = "policy_term")
    private Integer policyTerm;

    @Column(name = "premium_rate")
    private Double premiumRate;

    @Column(name = "sum_assured_death")
    private Double sumAssuredDeath;

    @Column(name = "sum_assured_tpd")
    private Double sumAssuredTPD;

    @Column(name = "sum_assured_add")
    private Double sumAssuredADD;

    @Column(name = "sum_assured_hosp_income")
    private Double sumAssuredHospIncome;

    @Column(name = "sum_assured_ci")
    private Double sumAssuredCI;

    @Column(name = "sum_assured_medic")
    private Double sumAssuredMedic;

    @Column(name = "sum_assured_cancer")
    private Double sumAssuredCancer;

    @Column(name = "product_weight_death")
    private Double productWeightDeath;

    @Column(name = "product_weight_pa")
    private Double productWeightPA;

    @Column(name = "product_weight_hosp_income")
    private Double productWeightHospIncome;

    @Column(name = "product_weight_ci")
    private Double productWeightCI;

    @Column(name = "product_weight_medic")
    private Double productWeightMedic;

    @Column(name = "product_weight_cancer")
    private Double productWeightCancer;

    @ManyToOne(optional = false)
    @NotNull
    private InsuranceCompany insuranceCompany;

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

    public Integer getEntryAgeLastBday() {
        return entryAgeLastBday;
    }

    public InsuranceProduct entryAgeLastBday(Integer entryAgeLastBday) {
        this.entryAgeLastBday = entryAgeLastBday;
        return this;
    }

    public void setEntryAgeLastBday(Integer entryAgeLastBday) {
        this.entryAgeLastBday = entryAgeLastBday;
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

    public Integer getPremiumTerm() {
        return premiumTerm;
    }

    public InsuranceProduct premiumTerm(Integer premiumTerm) {
        this.premiumTerm = premiumTerm;
        return this;
    }

    public void setPremiumTerm(Integer premiumTerm) {
        this.premiumTerm = premiumTerm;
    }

    public Integer getPolicyTerm() {
        return policyTerm;
    }

    public InsuranceProduct policyTerm(Integer policyTerm) {
        this.policyTerm = policyTerm;
        return this;
    }

    public void setPolicyTerm(Integer policyTerm) {
        this.policyTerm = policyTerm;
    }

    public Double getPremiumRate() {
        return premiumRate;
    }

    public InsuranceProduct premiumRate(Double premiumRate) {
        this.premiumRate = premiumRate;
        return this;
    }

    public void setPremiumRate(Double premiumRate) {
        this.premiumRate = premiumRate;
    }

    public Double getSumAssuredDeath() {
        return sumAssuredDeath;
    }

    public InsuranceProduct sumAssuredDeath(Double sumAssuredDeath) {
        this.sumAssuredDeath = sumAssuredDeath;
        return this;
    }

    public void setSumAssuredDeath(Double sumAssuredDeath) {
        this.sumAssuredDeath = sumAssuredDeath;
    }

    public Double getSumAssuredTPD() {
        return sumAssuredTPD;
    }

    public InsuranceProduct sumAssuredTPD(Double sumAssuredTPD) {
        this.sumAssuredTPD = sumAssuredTPD;
        return this;
    }

    public void setSumAssuredTPD(Double sumAssuredTPD) {
        this.sumAssuredTPD = sumAssuredTPD;
    }

    public Double getSumAssuredADD() {
        return sumAssuredADD;
    }

    public InsuranceProduct sumAssuredADD(Double sumAssuredADD) {
        this.sumAssuredADD = sumAssuredADD;
        return this;
    }

    public void setSumAssuredADD(Double sumAssuredADD) {
        this.sumAssuredADD = sumAssuredADD;
    }

    public Double getSumAssuredHospIncome() {
        return sumAssuredHospIncome;
    }

    public InsuranceProduct sumAssuredHospIncome(Double sumAssuredHospIncome) {
        this.sumAssuredHospIncome = sumAssuredHospIncome;
        return this;
    }

    public void setSumAssuredHospIncome(Double sumAssuredHospIncome) {
        this.sumAssuredHospIncome = sumAssuredHospIncome;
    }

    public Double getSumAssuredCI() {
        return sumAssuredCI;
    }

    public InsuranceProduct sumAssuredCI(Double sumAssuredCI) {
        this.sumAssuredCI = sumAssuredCI;
        return this;
    }

    public void setSumAssuredCI(Double sumAssuredCI) {
        this.sumAssuredCI = sumAssuredCI;
    }

    public Double getSumAssuredMedic() {
        return sumAssuredMedic;
    }

    public InsuranceProduct sumAssuredMedic(Double sumAssuredMedic) {
        this.sumAssuredMedic = sumAssuredMedic;
        return this;
    }

    public void setSumAssuredMedic(Double sumAssuredMedic) {
        this.sumAssuredMedic = sumAssuredMedic;
    }

    public Double getSumAssuredCancer() {
        return sumAssuredCancer;
    }

    public InsuranceProduct sumAssuredCancer(Double sumAssuredCancer) {
        this.sumAssuredCancer = sumAssuredCancer;
        return this;
    }

    public void setSumAssuredCancer(Double sumAssuredCancer) {
        this.sumAssuredCancer = sumAssuredCancer;
    }

    public Double getProductWeightDeath() {
        return productWeightDeath;
    }

    public InsuranceProduct productWeightDeath(Double productWeightDeath) {
        this.productWeightDeath = productWeightDeath;
        return this;
    }

    public void setProductWeightDeath(Double productWeightDeath) {
        this.productWeightDeath = productWeightDeath;
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

    public Double getProductWeightMedic() {
        return productWeightMedic;
    }

    public InsuranceProduct productWeightMedic(Double productWeightMedic) {
        this.productWeightMedic = productWeightMedic;
        return this;
    }

    public void setProductWeightMedic(Double productWeightMedic) {
        this.productWeightMedic = productWeightMedic;
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
