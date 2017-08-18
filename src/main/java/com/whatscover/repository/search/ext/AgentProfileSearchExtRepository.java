package com.whatscover.repository.search.ext;

import com.whatscover.domain.AgentProfile;
import com.whatscover.domain.CustomerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by minh on 02/08/2017.
 */
public interface AgentProfileSearchExtRepository {
    Page<AgentProfile> searchByName(String queryData, Pageable pageable);
}
