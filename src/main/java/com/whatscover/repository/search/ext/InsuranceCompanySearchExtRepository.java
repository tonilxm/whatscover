package com.whatscover.repository.search.ext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by toni on 31/07/2017.
 */
public interface InsuranceCompanySearchExtRepository<InsuranceCompany, Long> {
    Page<InsuranceCompany> searchByName(String name, Pageable pageable);
}
