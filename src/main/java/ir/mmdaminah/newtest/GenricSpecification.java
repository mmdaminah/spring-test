package ir.mmdaminah.newtest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class GenricSpecification {

    public static <T> Specification<T> byFields(Map<String, Object> filters, EntityManager em, Class<T> clazz) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            Metamodel metamodel = em.getMetamodel();
            EntityType<T> entityType = metamodel.entity(clazz);

            for(Map.Entry<String, Object> entry : filters.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();
                if(value == null) continue;

                if(entityType.getSingularAttribute(field) != null) {
                    predicate = cb.and(predicate, cb.like(root.get(field), "%" + value + "%"));
                }

            }
            return predicate;
        };
    }

}
