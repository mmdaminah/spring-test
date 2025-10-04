package ir.mmdaminah.newtest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class GenericSpecification4 {

    public static <T> Specification<T> search(Map<String, Object> filters, EntityManager em, Class<T> clazz) {
        return (root, query, cb) -> {

            Predicate predicate = cb.conjunction();
            Metamodel metaModel = em.getMetamodel();
            EntityType<T> entityType = metaModel.entity(clazz);

            Map<String, Map<String, Object>> rangeMap = new HashMap<>();

            filters.forEach((key, value) -> {
                if (key.contains(".")) {
                    String[] parts = key.split("\\.");
                    var field = parts[0];
                    var bound = parts[1];
                    rangeMap.computeIfAbsent(field, k -> new HashMap<>()).put(bound, value);
                }
            });

            for (var attr : entityType.getDeclaredAttributes()) {
                String field = attr.getName();
                Class<?> type = attr.getJavaType();

                Map<String, Object> bounds = rangeMap.get(field);

                if (Number.class.isAssignableFrom(type) || type.isPrimitive()) {
                    if (bounds != null) {
                        if (bounds.containsKey("min")) {
                            predicate = cb.and(predicate, cb.ge(root.get(field), Double.parseDouble(bounds.get("min").toString())));
                        }
                        if (bounds.containsKey("max")) {
                            predicate = cb.and(predicate, cb.le(root.get(field), Double.parseDouble(bounds.get("max").toString())));
                        }
                    } else if (filters.containsKey(field)) {
                        predicate = cb.and(predicate, cb.equal(root.get(field), Double.parseDouble(filters.get(field).toString())));
                    }
                } else if (String.class.isAssignableFrom(type)) {
                    if (filters.containsKey(field)) {
                        predicate = cb.and(predicate, cb.like(cb.lower(root.get(field)), "%" + filters.get(field).toString().toLowerCase() + "%"));
                    }
                } else if (java.util.Date.class.isAssignableFrom(type) || java.time.temporal.Temporal.class.isAssignableFrom(type)) {
                    if (bounds != null) {
                        if (bounds.containsKey("from")) {
                            String fromStr = bounds.get("from").toString();
                            LocalDateTime from = LocalDate.parse(fromStr).atStartOfDay();
                            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get(field), from));
                        }
                        if (bounds.containsKey("to")) {
                            String toStr = bounds.get("to").toString();
                            LocalDateTime to = LocalDate.parse(toStr).atTime(23, 59, 59);
                            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get(field), to));
                        }
                    }
                }

            }
            return predicate;
        };
    }

}
