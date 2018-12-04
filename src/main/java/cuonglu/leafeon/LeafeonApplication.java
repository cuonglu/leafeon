package cuonglu.leafeon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages ="cuonglu.leafeon.repository")
@EnableNeo4jRepositories(basePackages ="cuonglu.leafeon.neo4j.repository")
@EntityScan(basePackages = {"cuonglu.leafeon.neo4j.entity", "cuonglu.leafeon.entity"})
@EnableTransactionManagement
public class LeafeonApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeafeonApplication.class, args);
    }


}
