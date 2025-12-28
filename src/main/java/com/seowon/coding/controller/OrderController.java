package com.seowon.coding.controller;

import com.seowon.coding.domain.model.Order;
import com.seowon.coding.dto.CreateOrderRequest;
import com.seowon.coding.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        try {
            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(
            @RequestBody @Valid CreateOrderRequest request
    ) {
        // 1. customerName, 2. customerEmail 3. productIds, 4. quantities
        List<Long> productsIds = request.getProducts().stream()
                .map(CreateOrderRequest.Products::getProductId)
                .toList();

        List<Integer> quantities = request.getProducts().stream()
                .map(CreateOrderRequest.Products::getQuantity)
                .toList();

        Order createdOrder =
                orderService.placeOrder(request.getCustomerName(), request.getCustomerEmail(), productsIds, quantities);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}