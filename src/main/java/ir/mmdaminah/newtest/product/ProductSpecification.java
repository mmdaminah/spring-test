package ir.mmdaminah.newtest.product;

import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<Product> byName(String name) {
        if( name == null ) return null;
        return (root, query, cb) -> cb.like(root.get("name"),"%" + name + "%");
    }

    public static Specification<Product> byDescription(String description) {
        if( description == null ) return null;
        return (root, query, cb) -> cb.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<Product> byCategory(String category) {
        if( category == null ) return null;
        return (root, query, cb) -> cb.like(root.get("category"), "%" + category  + "%");
    }

    public static Specification<Product> byMinPrice(Double minPrice) {
        if( minPrice == null ) return null;
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> byMaxPrice(Double maxPrice) {
        if( maxPrice == null ) return null;
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

}
