package com.restaurant.delivery.controller;

import com.restaurant.delivery.dto.DishDTO;
import com.restaurant.delivery.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping("/newDish")
    public ResponseEntity<DishDTO> newDish(@RequestBody DishDTO dish){
        return ResponseEntity.status(HttpStatus.CREATED).body(dishService.createDish(dish));
    }

    @GetMapping("/allDishes")
    public ResponseEntity<List<DishDTO>> allDishes(){
        return ResponseEntity.ok(dishService.findAllDishes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDTO> findDishId(@PathVariable Integer id){
        return ResponseEntity.ok(dishService.findDishById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DishDTO> updateDish(@PathVariable Integer id, @RequestBody DishDTO update){
        return ResponseEntity.ok(dishService.updateDish(id, update));
    }

    @DeleteMapping("/deleteDish")
    public ResponseEntity<Void> deleteDish(@PathVariable Integer id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }

}
