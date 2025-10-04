package ir.mmdaminah.newtest.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Page<Product> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Specification<Product> spec = Specification
                .<Product>unrestricted()
                .and(ProductSpecification.byName(name))
                .and(ProductSpecification.byDescription(description))
                .and(ProductSpecification.byCategory(category))
                .and(ProductSpecification.byMinPrice(minPrice))
                .and(ProductSpecification.byMaxPrice(maxPrice));

        var pageable = PageRequest.of(page, size);

        return productRepository.findAll(spec, pageable);
    }

}
