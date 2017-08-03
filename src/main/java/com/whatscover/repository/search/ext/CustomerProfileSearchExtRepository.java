package com.whatscover.repository.search.ext;

import com.whatscover.domain.CustomerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by toni on 01/08/2017.
 */
public interface CustomerProfileSearchExtRepository {
    Page<CustomerProfile> searchByName(String name, Pageable pageable);
}
