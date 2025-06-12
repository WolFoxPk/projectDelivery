package com.restaurant.delivery.repository;

import com.restaurant.delivery.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<DishEntity, Integer> {

}
