package kafka.notificationservice.services;

import kafka.notificationservice.entity.Notification;
import kafka.notificationservice.entity.OrderEvent;
import kafka.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "orders", groupId = "notification-group")
    public void handleOrderEvent(OrderEvent event) {
        Notification notification = new Notification();
        notification.setOrderId(event.getOrderId());
        notification.setMessage("Order " + event.getOrderId() + " has been " + event.getStatus());
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }
}
