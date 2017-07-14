package com.whatscover.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whatscover.domain.InsuranceCompany;

/**
 * Spring Data JPA repository for the InsuranceCompany entity.
 */
@Repository
public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany, Long> {

	Optional<InsuranceCompany> findOneByCode(String code);
}
