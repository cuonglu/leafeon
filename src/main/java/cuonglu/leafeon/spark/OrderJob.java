package cuonglu.leafeon.spark;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cuonglu.leafeon.model.OrderItem;
import cuonglu.leafeon.neo4j.entity.Product;
import cuonglu.leafeon.neo4j.entity.Related;
import cuonglu.leafeon.neo4j.repository.Neo4jProductRepository;
import cuonglu.leafeon.neo4j.repository.Neo4jRelatedRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class OrderJob {

    @Autowired
    private final JavaStreamingContext jssc;
    @Autowired
    private final Neo4jProductRepository productRepository;
    @Autowired
    private final Neo4jRelatedRepository relatedRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {

        String brokers = "localhost:9092";
        String topics = "order-topic";

        HashSet<String> topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));

        HashMap<String, Object> kafkaParams = new HashMap<>();

        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, "use_a_separate_group_id_for_each_stream");
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);


        JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(
                jssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topicsSet, kafkaParams));
        messages.map(ConsumerRecord::value).foreachRDD(rdd -> {
            if (rdd.isEmpty()) {
                return;
            }
            List<OrderItem> items = rdd.collect().stream().flatMap(item -> {
                try {
                    List<OrderItem> tmp = objectMapper.readValue(item, new TypeReference<List<OrderItem>>() {});
                    return tmp.stream();
                } catch (IOException e) {
                    return new ArrayList<OrderItem>().stream();
                }
            }).collect(Collectors.toList());

            Iterable<Product> products = productRepository.findAllById(items.stream().map(OrderItem::getProductId).collect(Collectors.toList()));
            Map<String, Product> currentProductByIds = StreamSupport.stream(products.spliterator(), false)
                    .collect(Collectors.toMap(Product::getProductId, o -> o));

            Set<Product> fullProductByIds = items.stream()
                    .map(item -> {
                        if (currentProductByIds.containsKey(item.getProductId())) {
                            return currentProductByIds.get(item.getProductId());
                        } else {
                            return productRepository.save(Product.builder().productId(item.getProductId()).name(item.getProductId()).build());
                        }
                    })
                    .collect(Collectors.toSet());

            Set<Product> temp = new HashSet<>(fullProductByIds);
            fullProductByIds.forEach(product -> {
                temp.remove(product);
                temp.forEach(tempProduct -> {
                    Related related = relatedRepository.findRelative(product.getProductId(), tempProduct.getProductId())
                            .orElseGet(() -> Related.builder().start(product).end(tempProduct).weight(0).build());
                    related.increaseWeight();
                    relatedRepository.save(related);
                });
            });
        });
        jssc.start();
    }
}
