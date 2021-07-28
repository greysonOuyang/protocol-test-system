package com.yuyi.pts.repository;

import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * description
 *
 * @author greyson
 * @since 2021/7/28
 */
@NoRepositoryBean
public class BaseRepository {
    @PersistenceContext
    protected EntityManager em;
}
