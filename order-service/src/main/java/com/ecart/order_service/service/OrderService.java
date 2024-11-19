package com.ecart.order_service.service;

import com.ecart.order_service.dto.InventoryResponse;
import com.ecart.order_service.dto.OrderItemsDto;
import com.ecart.order_service.dto.OrderRequest;
import com.ecart.order_service.dto.OrderResponse;
import com.ecart.order_service.entity.Order;
import com.ecart.order_service.entity.OrderItems;
import com.ecart.order_service.event.OrderPlacedEvent;
import com.ecart.order_service.publisher.OrderEventPublisher;
import com.ecart.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private OrderEventPublisher orderEventPublisher;
    public String placeOrder(OrderRequest orderRequest) {
        List<String> skuCodes = orderRequest.getOrderItemsList()
                                        .stream()
                                        .map(OrderItemsDto::getSkuCode).toList();
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://localhost:9093/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        assert inventoryResponses != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponses)
                                .allMatch(InventoryResponse::isInStock);
        if(allProductsInStock) {
            Order order = orderRepository.save(orderMapper(orderRequest));
            orderEventPublisher.sendOrderPlacedEvent(new OrderPlacedEvent(order.getOrderNumber()));
            return "Order placed successfully";
        }
        else
            throw new IllegalArgumentException("Product is not in stock, please try again later");
    }

    private Order orderMapper(OrderRequest orderRequest){
        return Order.builder().orderItemsList(orderRequest.getOrderItemsList()
                .stream()
                .map(this::orderItemsMapper)
                .toList())
                .orderNumber(UUID.randomUUID().toString())
                .build();
    }

    private OrderItems orderItemsMapper(OrderItemsDto orderItemsDto) {
        return OrderItems.builder()
                .price(orderItemsDto.getPrice())
                .quantity(orderItemsDto.getQuantity())
                .skuCode(orderItemsDto.getSkuCode())
                .build();
    }

    public List<OrderResponse> getAllOrders(){
        return orderRepository.findAll().stream().map(order -> OrderResponse.builder()
                .orderNumber(order.getOrderNumber())
                .orderItemsList(order.getOrderItemsList()
                                .stream()
                                .map(orderItem -> OrderItems.builder().skuCode(orderItem.getSkuCode())
                                        .quantity(orderItem.getQuantity())
                                        .id(orderItem.getId())
                                        .price(orderItem.getPrice())
                                        .build()).toList())
                .build()).toList();
    }
}
