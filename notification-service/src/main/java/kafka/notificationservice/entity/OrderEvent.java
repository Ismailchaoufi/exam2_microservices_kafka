package kafka.notificationservice.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderEvent {
    private Long orderId;
    private String status;

    // Optional: Add more fields as needed
    private LocalDateTime timestamp;
    private String customerEmail;
    private Double totalAmount;
}
