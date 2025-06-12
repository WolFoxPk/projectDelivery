package com.restaurant.delivery.repository;

import com.restaurant.delivery.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.orderDishEntities")
    List<OrderEntity> findAllWithDishes();
}
