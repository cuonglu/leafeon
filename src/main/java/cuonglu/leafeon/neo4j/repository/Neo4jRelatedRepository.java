package cuonglu.leafeon.neo4j.repository;

import cuonglu.leafeon.neo4j.entity.Related;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Neo4jRelatedRepository extends Neo4jRepository<Related, Long> {
    @Query("MATCH (p1:Product {productId:{productId1}})-[r:RELATED]-(p2:Product {productId:{productId2}}) RETURN p1, p2, r")
    Optional<Related> findRelative(@Param("productId1") String id1, @Param("productId2") String i2);
}
