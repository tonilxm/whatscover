package com.whatscover.service.dto;


import java.util.Objects;

import javax.validation.constraints.Size;

/**
 * A DTO for the ProductCategory entity.
 */
public class ProductCategoryDTO extends AbstractAuditingDTO {

    private Long id;

    @Size(max = 100)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductCategoryDTO productCategoryDTO = (ProductCategoryDTO) o;
        if(productCategoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productCategoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
