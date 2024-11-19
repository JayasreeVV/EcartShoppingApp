package com.ecart.order_service.controller;

import com.ecart.order_service.dto.OrderRequest;
import com.ecart.order_service.dto.OrderResponse;
import com.ecart.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    @ResponseStatus(HttpStatus.CREATED)
    //@CircuitBreaker(name = "inventory", fallbackMethod = "fallBack")
    //@TimeLimiter(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        return CompletableFuture.supplyAsync(() -> {
            try {
                return orderService.placeOrder(orderRequest);
            } catch (Exception e) {
                throw new RuntimeException("Error placing order: " + e.getMessage());
            }
        });
    }

    private CompletableFuture<String> fallBack(OrderRequest orderRequest, RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() ->
                "Oops! Something went wrong: " + runtimeException.getMessage());
    }

    @GetMapping("/allOrders")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders(){
        return orderService.getAllOrders();
    }
}
