package cuonglu.leafeon.neo4j.repository;

import cuonglu.leafeon.neo4j.entity.Related;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Neo4jRelatedRepository extends Neo4jRepository<Related, Long> {

}
