package com.restaurant.delivery.controller;

import com.restaurant.delivery.dto.OrderRequestDTO;
import com.restaurant.delivery.dto.OrderResponseDTO;
import com.restaurant.delivery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/newOrder")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderDtoIn){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDtoIn));
    }

    @GetMapping("/allOrders")
    public ResponseEntity<List<OrderResponseDTO>> allOrders() {
        return ResponseEntity.ok(orderService.findAllOrder());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findOrderById(@PathVariable Integer id){
        return ResponseEntity.ok(orderService.findOrderById(id));
    }


    @DeleteMapping("/deleteOrder/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id){
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
