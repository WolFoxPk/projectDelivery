package com.restaurant.delivery.mapper;

import com.restaurant.delivery.dto.DishDTO;
import com.restaurant.delivery.entity.DishEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface DishMapper {


    default DishEntity fromDtoToEntity(DishDTO dishRequestDto){
        if (dishRequestDto == null){
            return null;
        }
        DishEntity dishEntity = new DishEntity();
        dishEntity.setId(dishRequestDto.getId());
        dishEntity.setName(dishRequestDto.getName());
        dishEntity.setDescription(dishRequestDto.getDescription());
        dishEntity.setPrice(dishRequestDto.getPrice());
        dishEntity.setDishType(dishRequestDto.getDishType());
        return dishEntity;
    }

    default DishDTO fromEntityToDTO(DishEntity dishEntity){
        if (dishEntity == null) {
            return null;
        }

        DishDTO dishRequestDtoOut = new DishDTO();
        dishRequestDtoOut.setId(dishEntity.getId());
        dishRequestDtoOut.setName(dishEntity.getName());
        dishRequestDtoOut.setDescription(dishEntity.getDescription());
        dishRequestDtoOut.setPrice(dishEntity.getPrice());
        dishRequestDtoOut.setDishType(dishEntity.getDishType());

        return dishRequestDtoOut;
    }

    default List<DishDTO> fromEntityListToDtoList(Collection<DishEntity> dishEntityList){
        return dishEntityList.stream().map(this::fromEntityToDTO).toList();
    };
   default List<DishEntity> fromDtoListToEntityList(Collection<DishDTO> dishDtoList){
        return  dishDtoList.stream().map(this::fromDtoToEntity).toList();
    };
}
