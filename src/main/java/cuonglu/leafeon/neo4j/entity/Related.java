package cuonglu.leafeon.neo4j.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RelationshipEntity(type = "RELATED")
public class Related {

    @Id
    @GeneratedValue
    private Long id;

    private Integer weight;

    @StartNode
    private Product start;

    @EndNode
    private Product end;

    public void increaseWeight() {
        if (weight == null) {
            weight = 0;
        }
        weight++;
    }
}
