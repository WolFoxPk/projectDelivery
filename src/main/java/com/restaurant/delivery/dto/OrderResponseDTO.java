package com.restaurant.delivery.dto;

import com.restaurant.delivery.entity.DeliveryType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Integer orderId;
    private List<DishDTO> dishes;
    private BigDecimal total;
    private BigDecimal baseTotal;
    private LocalDateTime creationDate;
    private DeliveryType deliveryType;
    private boolean withReservation;
    private Integer partecipants;
    private BigDecimal surcharge;
    private String summary;
}
