package ir.mmdaminah.newtest.user;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final Faker faker = new Faker();
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0) {
            var users = new ArrayList<User>();
            for (int i = 0; i < 1000; i++) {
                var user = new User();
                user.setFirstName(faker.name().firstName());
                user.setLastName(faker.name().lastName());
                user.setEmail(faker.internet().emailAddress());
                user.setPassword(faker.internet().password());
                user.setPhone(faker.phoneNumber().phoneNumber());
                user.setUsername(faker.name().username());
                user.setAddress(faker.address().streetAddress());
                user.setCreatedAt(faker.date().birthday());
                user.setAge(faker.number().numberBetween(1, 100));
                users.add(user);
            }
            userRepository.saveAll(users);
        }
    }
}
