package com.whatscover.repository;

import com.whatscover.domain.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the CustomerProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile,Long> {

}
