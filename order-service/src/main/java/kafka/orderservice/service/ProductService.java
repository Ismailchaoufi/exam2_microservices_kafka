package kafka.orderservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import kafka.orderservice.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
    public ProductDTO getProduct(Long id) {
        return restTemplate.getForObject(
                "http://localhost:8081/api/products/" + id,
                ProductDTO.class
        );
    }

    private ProductDTO getProductFallback(Long id, Exception ex) {
        return new ProductDTO(); // Return default or cached product
    }
}
