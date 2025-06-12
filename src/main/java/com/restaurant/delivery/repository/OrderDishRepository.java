package com.restaurant.delivery.repository;

import com.restaurant.delivery.entity.OrderDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDishRepository extends JpaRepository<OrderDishEntity, Integer> {

}
