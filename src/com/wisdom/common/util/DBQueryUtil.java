package com.wisdom.common.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

@Component
@Transactional
public class DBQueryUtil {

	@PersistenceContext
	private EntityManager entityManager;
	
	public <T> List<T> getQueryOutput(String query, String[] params, Object[] values, Class<T> type) {

		TypedQuery<T> regionTitleQuery = entityManager.createQuery(query, type);
		for (int qIdx = 0; qIdx < params.length; qIdx++) {
			regionTitleQuery.setParameter(params[qIdx], values[qIdx]);
		}

		return regionTitleQuery.getResultList();
	}
}
