package com.whatscover.repository;

import com.whatscover.repository.ext.CustomerProfileExtRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by toni on 01/08/2017.
 */
public class CustomerProfileRepositoryImpl implements CustomerProfileExtRepository{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void deleteByUserId(Long userId) {
        String deleteSql = "delete from CustomerProfile cp where cp.user.id=:userId";
        entityManager.createQuery(deleteSql).setParameter("userId", userId).executeUpdate();
    }
}
