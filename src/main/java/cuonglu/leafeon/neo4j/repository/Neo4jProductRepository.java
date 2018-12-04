package cuonglu.leafeon.neo4j.repository;

import cuonglu.leafeon.neo4j.entity.Product;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Neo4jProductRepository extends Neo4jRepository<Product, String> {
    @Query("MATCH (:Product {productId:{productId}})-[r:RELATED]-(p:Product) RETURN p ORDER BY r.weight")
    List<Product> getRelativeProduct(@Param("productId") String id);
}

