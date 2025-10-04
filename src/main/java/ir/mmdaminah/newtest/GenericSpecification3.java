package ir.mmdaminah.newtest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.Map;

public class GenericSpecification3 {

    public static <T> Specification<T> search(Map<String, Object> filters, EntityManager em, Class<T> clazz) {
        return (root, query, cb) -> {

            Predicate predicate = cb.conjunction();
            Metamodel metaModel = em.getMetamodel();
            EntityType<T> entityType = metaModel.entity(clazz);

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                if (value == null) continue;

                SingularAttribute<T, ?> attr = entityType.getDeclaredSingularAttribute(field);
                Class<?> filedType = attr.getJavaType();

                if (String.class.isAssignableFrom(filedType)) {
                    predicate = cb.and(predicate, cb.like(root.get(field), "%" + value.toString().toLowerCase() + "%"));
                } else if (Number.class.isAssignableFrom(filedType)) {
                    if (value instanceof Map<?, ?> range) {
                        Number min = (Number) range.get("min");
                        Number max = (Number) range.get("max");
                        if (min != null) {
                            predicate = cb.and(predicate, cb.ge(root.get(field), min));
                        }
                        if (max != null) {
                            predicate = cb.and(predicate, cb.le(root.get(field), max));
                        }
                    } else {
                        predicate = cb.and(predicate, cb.equal(root.get(field), value));
                    }
                } else if (java.util.Date.class.isAssignableFrom(filedType) || java.time.temporal.Temporal.class.isAssignableFrom(filedType)) {

                    if (value instanceof Map<?, ?> range) {
                        Comparable from = (Comparable) range.get("from");
                        Comparable to = (Comparable) range.get("to");
                        if (from != null) {
                            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get(field), from));
                        }
                        if (to != null) {
                            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get(field), to));
                        }
                    } else {
                        predicate = cb.and(predicate, cb.equal(root.get(field), value));
                    }

                }


            }


            return predicate;

        };
    }

}
