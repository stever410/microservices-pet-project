package com.ducnt.orderservice.services;

import com.ducnt.orderservice.dtos.InventoryResponse;
import com.ducnt.orderservice.dtos.OrderLineItemDto;
import com.ducnt.orderservice.dtos.OrderRequest;
import com.ducnt.orderservice.models.Order;
import com.ducnt.orderservice.models.OrderLineItem;
import com.ducnt.orderservice.repositories.OrderRepository;
import com.ducnt.orderservice.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    public Order createOrder(OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems().stream()
                .map(this::mapToOrderItem)
                .collect(Collectors.toList());
        Order order = new Order();
        order.setLineItems(orderLineItems);
        order.setOrderNumber(AppUtils.generateOrderNumber());

        List<String> skuCodes = order.getLineItems().stream()
                .map(OrderLineItem::getSkuCode)
                .collect(Collectors.toList());

        // Call inventory service and create order if product in stock
        var inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventories",
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            return orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderLineItem mapToOrderItem(OrderLineItemDto orderLineItemDto) {
        return OrderLineItem.builder()
                .id(orderLineItemDto.getId())
                .price(orderLineItemDto.getPrice())
                .quantity(orderLineItemDto.getQuantity())
                .skuCode(orderLineItemDto.getSkuCode())
                .build();
    }
}
