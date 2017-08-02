package com.whatscover.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A InsuranceProductPremiumRate.
 */
@Entity
@Table(name = "insurance_product_premium_rate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "insuranceproductpremiumrate")
public class InsuranceProductPremiumRate extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "entry_age", nullable = false)
    private Integer entryAge;

    @NotNull
    @Column(name = "male_premium_rate", nullable = false)
    private Double malePremiumRate;

    @NotNull
    @Column(name = "female_premium_rate", nullable = false)
    private Double femalePremiumRate;

    @Size(max = 5)
    @Column(name = "jhi_plan", length = 5)
    private String plan;

    @ManyToOne(optional = false)
    @NotNull
    private InsuranceProduct insuranceProduct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEntryAge() {
        return entryAge;
    }

    public InsuranceProductPremiumRate entryAge(Integer entryAge) {
        this.entryAge = entryAge;
        return this;
    }

    public void setEntryAge(Integer entryAge) {
        this.entryAge = entryAge;
    }

    public Double getMalePremiumRate() {
        return malePremiumRate;
    }

    public InsuranceProductPremiumRate malePremiumRate(Double malePremiumRate) {
        this.malePremiumRate = malePremiumRate;
        return this;
    }

    public void setMalePremiumRate(Double malePremiumRate) {
        this.malePremiumRate = malePremiumRate;
    }

    public Double getFemalePremiumRate() {
        return femalePremiumRate;
    }

    public InsuranceProductPremiumRate femalePremiumRate(Double femalePremiumRate) {
        this.femalePremiumRate = femalePremiumRate;
        return this;
    }

    public void setFemalePremiumRate(Double femalePremiumRate) {
        this.femalePremiumRate = femalePremiumRate;
    }

    public String getPlan() {
        return plan;
    }

    public InsuranceProductPremiumRate plan(String plan) {
        this.plan = plan;
        return this;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public InsuranceProduct getInsuranceProduct() {
        return insuranceProduct;
    }

    public InsuranceProductPremiumRate insuranceProduct(InsuranceProduct insuranceProduct) {
        this.insuranceProduct = insuranceProduct;
        return this;
    }

    public void setInsuranceProduct(InsuranceProduct insuranceProduct) {
        this.insuranceProduct = insuranceProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InsuranceProductPremiumRate insuranceProductPremiumRate = (InsuranceProductPremiumRate) o;
        if (insuranceProductPremiumRate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), insuranceProductPremiumRate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InsuranceProductPremiumRate{" +
            "id=" + getId() +
            ", entryAge='" + getEntryAge() + "'" +
            ", malePremiumRate='" + getMalePremiumRate() + "'" +
            ", femalePremiumRate='" + getFemalePremiumRate() + "'" +
            ", plan='" + getPlan() + "'" +
            "}";
    }
}
