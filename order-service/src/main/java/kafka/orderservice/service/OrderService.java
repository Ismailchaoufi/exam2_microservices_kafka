package kafka.orderservice.service;

import kafka.orderservice.dto.ProductDTO;
import kafka.orderservice.entity.Order;
import kafka.orderservice.entity.OrderEvent;
import kafka.orderservice.entity.OrderItem;
import kafka.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public Order createOrder(Order order) {
        // Verify products availability
        for (OrderItem item : order.getItems()) {
            ProductDTO product = productService.getProduct(item.getProductId());
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }
        }

        order.setStatus("CREATED");
        Order savedOrder = orderRepository.save(order);

        // Send event to Kafka
        kafkaTemplate.send("orders", new OrderEvent(savedOrder.getId(), "CREATED"));

        return savedOrder;
    }
}