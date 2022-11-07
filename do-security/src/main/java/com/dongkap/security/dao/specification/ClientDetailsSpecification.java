package com.dongkap.security.dao.specification;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.dongkap.security.entity.ClientDetailsEntity;


public class ClientDetailsSpecification {

	public static Specification<ClientDetailsEntity> getDatatable(final Map<String, Object> keyword) {
		return new Specification<ClientDetailsEntity>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -637621292944403277L;

			@Override
			public Predicate toPredicate(Root<ClientDetailsEntity> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
				Predicate predicate = builder.conjunction();
				if (!keyword.isEmpty()) {
					for(Map.Entry<String, Object> filter : keyword.entrySet()) {
						String key = filter.getKey();
						Object value = filter.getValue();
						if (value != null) {
							switch (key) {
								case "clientId" :
									predicate.getExpressions().add(builder.equal(root.<String>get(key), value.toString()));
									break;
								case "_all" :
									predicate.getExpressions().add(builder.equal(root.<String>get("clientId"), value.toString()));
									break;
								default :
									break;
							}	
						}
					}
				}
				return predicate;
			}
		};
	}

}
