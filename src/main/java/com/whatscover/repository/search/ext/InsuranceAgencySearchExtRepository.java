package com.whatscover.repository.search.ext;

import com.whatscover.domain.InsuranceAgency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InsuranceAgencySearchExtRepository {
	Page<InsuranceAgency> searchByName(String name, Pageable pageable);
}
