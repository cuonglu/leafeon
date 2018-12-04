package cuonglu.leafeon.mocking;

import cuonglu.leafeon.entity.Category;
import cuonglu.leafeon.entity.Product;
import cuonglu.leafeon.repository.CategoryRepository;
import cuonglu.leafeon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
public class MockingComponent {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final ProductRepository productRepository;


    @EventListener(ApplicationReadyEvent.class)
    public void generate(final ApplicationReadyEvent event) {
        Category fastFood = new Category();
        fastFood.setId("F01");
        fastFood.setName("Fast Food");
        fastFood.setCreateDate(new Date());
        categoryRepository.save(fastFood);

        Category seaFood = new Category();
        seaFood.setId("F02");
        seaFood.setName("Sea Food");
        seaFood.setCreateDate(new Date());
        categoryRepository.save(seaFood);

        String[] ff = {"Hamburger", "Pizza", "Sandwich"};
        AtomicInteger index = new AtomicInteger(1);
        Stream.of(ff).forEach(ffName -> {
                    Product product = new Product();
                    product.setCategoryId(fastFood);
                    product.setId(fastFood.getId() + StringUtils.leftPad(String.valueOf(index.getAndIncrement()), 4, "0"));
                    product.setName(ffName);
                    product.setPrice(new Random().nextInt(500));
                    product.setCreateDate(new Date());
                    productRepository.save(product);
                }
        );

        index.set(1);
        String[] sf = {"Sushi", "Fish", "Crabs"};
        Stream.of(sf).forEach(sfName -> {
                    Product product = new Product();
                    product.setCategoryId(seaFood);
                    product.setId(seaFood.getId() + StringUtils.leftPad(String.valueOf(index.getAndIncrement()), 4, "0"));
                    product.setName(sfName);
                    product.setPrice(new Random().nextInt(500));
                    product.setCreateDate(new Date());
                    productRepository.save(product);
                }
        );
    }
}
