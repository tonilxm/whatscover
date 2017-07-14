package com.whatscover.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A InsuranceCompany.
 */
@Entity
@Table(name = "insurance_company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "insurancecompany")
public class InsuranceCompany extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "code", length = 100, nullable = false, unique = true)
    private String code;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @Size(max = 200)
    @Column(name = "description", length = 200)
    private String description;

    @Size(max = 200)
    @Column(name = "address_1", length = 200)
    private String address_1;

    @Size(max = 200)
    @Column(name = "address_2", length = 200)
    private String address_2;

    @Size(max = 200)
    @Column(name = "address_3", length = 200)
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

    public InsuranceCompany code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public InsuranceCompany name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public InsuranceCompany description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress_1() {
        return address_1;
    }

    public InsuranceCompany address_1(String address_1) {
        this.address_1 = address_1;
        return this;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public InsuranceCompany address_2(String address_2) {
        this.address_2 = address_2;
        return this;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getAddress_3() {
        return address_3;
    }

    public InsuranceCompany address_3(String address_3) {
        this.address_3 = address_3;
        return this;
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
        InsuranceCompany insuranceCompany = (InsuranceCompany) o;
        if (insuranceCompany.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), insuranceCompany.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InsuranceCompany{" +
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
