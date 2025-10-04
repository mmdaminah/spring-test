package ir.mmdaminah.newtest.user;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> byFirstName(String firstName) {
        return (root, query, cb) -> {
            if (firstName == null) return null;
            return cb.like(root.get("firstName"), String.format("%%%s%%", firstName));
        };
    }

    public static Specification<User> byLastName(String lastName) {
        return (root, query, cb) ->{
            if (lastName == null) return null;
            return cb.like(root.get("lastName"), String.format("%%%s%%", lastName));
        };
    }

    public static Specification<User> byEmail(String email) {
        return (root, query, cb) -> {
            if(email == null) return null;
            return cb.like(root.get("email"), String.format("%%%s%%", email));
        };
    }

}
