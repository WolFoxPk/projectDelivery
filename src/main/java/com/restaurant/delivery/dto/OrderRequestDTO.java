package com.restaurant.delivery.dto;

import com.restaurant.delivery.entity.DeliveryType;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private List<Integer> dishListId;

    private DeliveryType deliveryType;
    private boolean withReservation;
    private Integer partecipants;

}
