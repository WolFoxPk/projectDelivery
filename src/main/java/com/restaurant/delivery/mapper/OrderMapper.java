package com.restaurant.delivery.mapper;

import com.restaurant.delivery.dto.OrderResponseDTO;
import com.restaurant.delivery.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {



    OrderResponseDTO fromEntitytoDTO(OrderEntity orderEntity);
    OrderEntity fromDtoToEntity(OrderResponseDTO orderResponseDTO);

    List<OrderResponseDTO> fromEntityListToDtoList(Collection<OrderEntity> orderEntityList);
    List<OrderEntity> fromDtoListToEntityList(Collection<OrderResponseDTO> orderResponseDtoList);
}
