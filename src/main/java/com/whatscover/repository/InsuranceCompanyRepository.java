package com.whatscover.repository;

import com.whatscover.domain.InsuranceCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the InsuranceCompany entity.
 */
@Repository
public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany,Long> {

	Optional<InsuranceCompany> findOneByCode(String code);
}
