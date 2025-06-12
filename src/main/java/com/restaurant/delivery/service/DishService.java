package com.restaurant.delivery.service;

import com.restaurant.delivery.dto.DishDTO;
import com.restaurant.delivery.entity.DishEntity;
import com.restaurant.delivery.mapper.DishMapper;
import com.restaurant.delivery.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepo;
    @Autowired
    private DishMapper dishMapper;

    public DishDTO createDish(DishDTO dishDtoIn){
        DishEntity newDish = dishMapper.fromDtoToEntity(dishDtoIn);
        DishEntity saveNewDish = dishRepo.save(newDish);
        return dishMapper.fromEntityToDTO(saveNewDish);
    }

    public DishDTO findDishById(Integer id){
        DishEntity dishId = dishRepo.findById(id).orElseThrow(()-> new NoSuchElementException("Dish not found with id: " + id));
        return dishMapper.fromEntityToDTO(dishId);
    }

    public List<DishDTO> findAllDishes(){
        List<DishEntity> allDishes = dishRepo.findAll();
        return dishMapper.fromEntityListToDtoList(allDishes);
    }

    public DishDTO updateDish(Integer id, DishDTO dishDtoIn) {
        DishEntity updateDish = dishRepo.findById(id).orElseThrow(()-> new NoSuchElementException("Dish not found with id: " + id));

        updateDish.setName(dishDtoIn.getName());
        updateDish.setDescription(dishDtoIn.getDescription());
        updateDish.setPrice(dishDtoIn.getPrice());
        updateDish.setDishType(dishDtoIn.getDishType());

        DishEntity update = dishRepo.save(updateDish);
        return dishMapper.fromEntityToDTO(update);
    }

    public void deleteDish(Integer id) {
        if(!dishRepo.existsById(id)) {
            throw new NoSuchElementException("Dish not found with id: " + id);
        }
        dishRepo.deleteById(id);
    }


}
