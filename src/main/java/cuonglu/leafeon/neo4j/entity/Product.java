package cuonglu.leafeon.neo4j.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NodeEntity
public class Product {

    @Id
    private String productId;

    private String name;

    @Relationship(type = "RELATED", direction = Relationship.UNDIRECTED)
    private Set<Related> related;
}
