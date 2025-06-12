package com.restaurant.delivery.dto;

import com.restaurant.delivery.entity.DishType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private DishType dishType;
}
