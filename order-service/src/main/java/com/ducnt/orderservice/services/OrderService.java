package com.ducnt.orderservice.services;

import com.ducnt.orderservice.dtos.OrderLineItemDto;
import com.ducnt.orderservice.dtos.OrderRequest;
import com.ducnt.orderservice.models.Order;
import com.ducnt.orderservice.models.OrderLineItem;
import com.ducnt.orderservice.repositories.OrderRepository;
import com.ducnt.orderservice.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems().stream()
                .map(this::mapToOrderItem)
                .collect(Collectors.toList());
        Order order = Order.builder()
                .orderNumber(AppUtils.generateOrderNumber())
                .lineItems(orderLineItems)
                .build();
        return orderRepository.save(order);
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
