package cuonglu.leafeon.controller;

import cuonglu.leafeon.model.RelativeProduct;
import cuonglu.leafeon.neo4j.entity.Product;
import cuonglu.leafeon.neo4j.entity.Related;
import cuonglu.leafeon.neo4j.repository.Neo4jProductRepository;
import cuonglu.leafeon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController("relative")
@RequiredArgsConstructor
public class RelativeContronller {

    @Autowired
    protected final Neo4jProductRepository neo4jProductRepository;

    @Autowired
    protected final ProductRepository productRepository;

    @GetMapping
    public List<RelativeProduct> getRelative(@RequestParam("productId") String id) {
        Optional<Product> product = neo4jProductRepository.findById(id);
        if (!product.isPresent() || product.get().getRelated() == null) {
            return Collections.emptyList();
        }

        Map<String, Integer> weight = product.get().getRelated().stream()
                .collect(Collectors.toMap(r -> {
                    String relativeProductId;
                    if (r.getStart().getProductId().equals(id)) {
                        relativeProductId = r.getEnd().getProductId();
                    } else {
                        relativeProductId = r.getStart().getProductId();
                    }
                    return relativeProductId;
                }, Related::getWeight));

        return StreamSupport.stream(productRepository.findAllById(weight.keySet()).spliterator(), false)
                .map(p -> {
                    RelativeProduct relativeProduct = new RelativeProduct();
                    relativeProduct.setWeight(weight.get(p.getId()));
                    relativeProduct.setId(p.getId());
                    relativeProduct.setName(p.getName());
                    relativeProduct.setCategoryId(p.getCategoryId());
                    relativeProduct.setPrice(p.getPrice());
                    relativeProduct.setCreateDate(p.getCreateDate());
                    return relativeProduct;
                })
                .sorted(Comparator.comparing(RelativeProduct::getWeight).reversed())
                .collect(Collectors.toList());
    }
}