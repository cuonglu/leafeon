package cuonglu.leafeon.neo4j.repository;

import cuonglu.leafeon.neo4j.entity.Product;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Neo4jProductRepository extends Neo4jRepository<Product, String> {
}

