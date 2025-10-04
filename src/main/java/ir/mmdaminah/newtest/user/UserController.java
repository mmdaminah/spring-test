package ir.mmdaminah.newtest.user;

import ir.mmdaminah.newtest.GenericSpecification3;
import ir.mmdaminah.newtest.GenericSpecification4;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @GetMapping
    public Page<User> getUsers(Pageable pageable, @RequestParam Map<String, Object> params) {
        Specification<User> specification = GenericSpecification4.search(params, entityManager, User.class);
        // Specification<User> specification = GenericSpecification3.search(params, entityManager, User.class);
        // Specification<User> specification = GenericSpecification2.search(params, entityManager, User.class);
        return userRepository.findAll(specification, pageable);
    }

}
