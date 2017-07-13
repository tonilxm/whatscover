package com.whatscover.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A InsuaranceAgency.
 */
@Entity
@Table(name = "insuarance_agency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "insuaranceagency")
public class InsuaranceAgency extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "code", length = 100, nullable = false)
    private String code;

    @NotNull
    @Column(name = "insuarance_company_id", nullable = false)
    private Integer insuarance_company_id;

    @Size(max = 200)
    @Column(name = "name", length = 200)
    private String name;

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

    public InsuaranceAgency code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getInsuarance_company_id() {
        return insuarance_company_id;
    }

    public InsuaranceAgency insuarance_company_id(Integer insuarance_company_id) {
        this.insuarance_company_id = insuarance_company_id;
        return this;
    }

    public void setInsuarance_company_id(Integer insuarance_company_id) {
        this.insuarance_company_id = insuarance_company_id;
    }

    public String getName() {
        return name;
    }

    public InsuaranceAgency name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress_1() {
        return address_1;
    }

    public InsuaranceAgency address_1(String address_1) {
        this.address_1 = address_1;
        return this;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public InsuaranceAgency address_2(String address_2) {
        this.address_2 = address_2;
        return this;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getAddress_3() {
        return address_3;
    }

    public InsuaranceAgency address_3(String address_3) {
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
        InsuaranceAgency insuaranceAgency = (InsuaranceAgency) o;
        if (insuaranceAgency.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), insuaranceAgency.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InsuaranceAgency{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", insuarance_company_id='" + getInsuarance_company_id() + "'" +
            ", name='" + getName() + "'" +
            ", address_1='" + getAddress_1() + "'" +
            ", address_2='" + getAddress_2() + "'" +
            ", address_3='" + getAddress_3() + "'" +
            "}";
    }
}
