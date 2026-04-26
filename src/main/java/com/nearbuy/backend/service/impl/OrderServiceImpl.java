package com.nearbuy.backend.service.impl;

import com.nearbuy.backend.dto.order.OrderResponse;
import com.nearbuy.backend.entity.*;
import com.nearbuy.backend.enums.OrderStatus;
import com.nearbuy.backend.exception.BadRequestException;
import com.nearbuy.backend.exception.ResourceNotFoundException;
import com.nearbuy.backend.repository.AddressRepository;
import com.nearbuy.backend.repository.CartRepository;
import com.nearbuy.backend.repository.OrderRepository;
import com.nearbuy.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public OrderResponse checkout(Long userId, Long addressId) {


        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }


        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!address.getUserId().equals(userId)) {
            throw new BadRequestException("Address does not belong to current user");
        }

        cart.getItems().forEach(item -> {
            Product product = item.getProduct();

            if (item.getQuantity() > product.getStock()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - item.getQuantity());
        });

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(item -> OrderItem.builder()
                        .product(item.getProduct())
                        .quantity(item.getQuantity())
                        .price(item.getProduct().getPrice())
                        .build())
                .toList();

        double total = orderItems.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();


        Order order = Order.builder()
                .userId(userId)
                .items(orderItems)
                .totalAmount(total)
                .address(address)
                .status(OrderStatus.PLACED)
                .build();

        orderItems.forEach(i -> i.setOrder(order));

        Order savedOrder = orderRepository.save(order);


        cart.getItems().clear();
        cartRepository.save(cart);

        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getUserOrders(Long userId) {

        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public OrderResponse updateStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // convert string → enum
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));

        Order updated = orderRepository.save(order);

        return mapToResponse(updated);
    }

    private OrderResponse mapToResponse(Order order) {

        return OrderResponse.builder()
                .orderId(order.getId())
                .products(
                        order.getItems().stream()
                                .map(i -> i.getProduct().getName())
                                .toList()
                )
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .address(order.getAddress().getStreet() + ", " + order.getAddress().getCity())
                .build();
    }
}
