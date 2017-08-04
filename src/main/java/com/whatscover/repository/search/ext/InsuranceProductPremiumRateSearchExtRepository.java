package com.whatscover.repository.search.ext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.whatscover.domain.InsuranceProductPremiumRate;

public interface InsuranceProductPremiumRateSearchExtRepository {
	Page<InsuranceProductPremiumRate> searchByInsuranceProductId(Long insuranceProductId, Pageable pageable);
}
