package com.whatscover.repository.search;

import com.whatscover.domain.ProductCategory;
import com.whatscover.repository.search.ext.ProductCategorySearchExtRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by toni on 28/07/2017.
 */
public class ProductCategorySearchRepositoryImpl implements ProductCategorySearchExtRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page searchByName(String name, Pageable pageable) {
        String searchSql = "select pc from ProductCategory pc where pc.name like :name";
        List<ProductCategory>  searchResultList = entityManager.createQuery(searchSql, ProductCategory.class)
                                                    .setParameter("name", "%" + name + "%")
                                                    .setFirstResult((pageable.getPageSize() * pageable.getPageNumber()))
                                                    .setMaxResults(pageable.getPageSize())
                                                    .getResultList();

        String totalRecordSql = "select count(*) from ProductCategory pc where pc.name like :name";
        Long totalRecord =  (Long) entityManager.createQuery(totalRecordSql).setParameter("name", "%" + name + "%")
            .getSingleResult();

        Page<ProductCategory> result = new PageImpl(searchResultList, pageable, totalRecord);

        return result;
    }
}
