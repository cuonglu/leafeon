package cuonglu.leafeon.controller;

import cuonglu.leafeon.model.OrderItem;
import cuonglu.leafeon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.util.IterableUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController("order")
@RequiredArgsConstructor
public class OrderController {

    @Value(value = "${kafka.order-topic}")
    private String orderTopic;

    @Autowired
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private final ProductRepository productRepository;

    @PostMapping
    public void create(@RequestBody List<OrderItem> items) {
        List<String> ids = items.stream().map(OrderItem::getProductId).collect(Collectors.toList());
        long length = IterableUtils.count(productRepository.findAllById(ids));
        if (length != ids.size()) {
            throw new RuntimeException("Order list contains product which isn't exist");
        }

        kafkaTemplate.send(orderTopic, items);
    }
}
