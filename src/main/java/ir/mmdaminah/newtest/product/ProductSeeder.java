package ir.mmdaminah.newtest.product;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final Faker faker = new Faker();

    public ProductSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(productRepository.count() == 0) {
            List<Product> products = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                var product = new Product();
                product.setName(faker.name().title());
                product.setDescription(faker.shakespeare().asYouLikeItQuote());
                product.setPrice(ThreadLocalRandom.current().nextDouble(100, 10000));
                product.setCategory(faker.book().genre());
                product.setBrand(faker.programmingLanguage().name());
                products.add(product);
            }
            productRepository.saveAll(products);
        }
    }
}
