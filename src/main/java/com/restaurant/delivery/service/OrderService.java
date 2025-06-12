package com.restaurant.delivery.service;

import com.restaurant.delivery.dto.DishDTO;
import com.restaurant.delivery.dto.OrderRequestDTO;
import com.restaurant.delivery.dto.OrderResponseDTO;
import com.restaurant.delivery.entity.DeliveryType;
import com.restaurant.delivery.entity.DishEntity;
import com.restaurant.delivery.entity.OrderDishEntity;
import com.restaurant.delivery.entity.OrderEntity;
import com.restaurant.delivery.mapper.DishMapper;
import com.restaurant.delivery.mapper.OrderMapper;
import com.restaurant.delivery.repository.DishRepository;
import com.restaurant.delivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    @Autowired
    private DishRepository dishRepo;
    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private OrderMapper orderMapper;

    private static final BigDecimal onlineDiscount = new BigDecimal("5.00");
    private static final BigDecimal reservationDiscount = new BigDecimal("2.00");
    private static final BigDecimal onlineRes = new BigDecimal("1.50");
    private static final BigDecimal takeAway = new BigDecimal("1.22");
    private static final BigDecimal coverCharge = new BigDecimal("2.00");
    private static final int maxDishes = 10;

    public OrderResponseDTO createOrder(OrderRequestDTO orderDtoIn) {

        validateOrderRequest(orderDtoIn);
        List<DishEntity> dishEntities = getAndValidateDishes(orderDtoIn.getDishListId());
        BigDecimal total = calculateTotal(orderDtoIn, dishEntities);
        OrderEntity order = buildOrderEntity(orderDtoIn, total, dishEntities);
        orderRepo.save(order);
        order = orderRepo.findById(order.getId()).orElseThrow();

        OrderEntity savedOrder = orderRepo.findById(order.getId()).orElseThrow(()-> new IllegalArgumentException("Order not found"));
        return mapToResponse(savedOrder);
    }

    private void validateOrderRequest(OrderRequestDTO dto) {
        if (dto.getDeliveryType() == DeliveryType.IN_HOUSE &&
                (dto.getPartecipants() == null || dto.getPartecipants() < 1)) {
            throw new IllegalArgumentException("Participants required and must be >= 1 for IN_HOUSE");
        }

        if (dto.getDishListId() == null || dto.getDishListId().size() > maxDishes) {
            throw new IllegalArgumentException("You cannot order more than 10 dishes!");
        }
    }

    private List<DishEntity> getAndValidateDishes(List<Integer> dishIds) {
        List<DishEntity> dishes = dishRepo.findAllById(dishIds);
        if (dishes.size() != dishIds.size()) {
            throw new NoSuchElementException("One or more dishes not found");
        }
        return dishes;
    }

    private BigDecimal calculateTotal(OrderRequestDTO dto, List<DishEntity> dishes) {
        BigDecimal total = dishes.stream()
                .map(DishEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return switch (dto.getDeliveryType()) {
            case ONLINE -> calculateOnlineTotal(total, dto.isWithReservation());
            case TAKE_AWAY -> total.multiply(takeAway);
            case IN_HOUSE -> total.add(coverCharge.multiply(new BigDecimal(dto.getPartecipants())));
        };
    }

    private BigDecimal calculateOnlineTotal(BigDecimal base, boolean withReservation){
        BigDecimal totalOnline = base.subtract(onlineDiscount);
        if(withReservation){
            totalOnline = totalOnline.subtract(reservationDiscount);
        }
        return totalOnline.add(onlineRes);
    }


    private OrderEntity buildOrderEntity(OrderRequestDTO dto, BigDecimal total, List<DishEntity> dishes) {
        OrderEntity order = new OrderEntity();
        order.setCreationDate(LocalDateTime.now());
        order.setDeliveryType(dto.getDeliveryType());
        order.setWithReservation(dto.isWithReservation());
        order.setPartecipants(dto.getPartecipants());
        order.setTotal(total);

        List<OrderDishEntity> orderDishEntities = dishes.stream()
                .map(dish -> buildOrderDish(order, dish)).toList();

        orderDishEntities.forEach(od -> od.setOrderEntity(order));
        order.setOrderDishEntities(orderDishEntities);

        return order;
    }

    private OrderResponseDTO mapToResponse(OrderEntity order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setCreationDate(order.getCreationDate());
        dto.setDeliveryType(order.getDeliveryType());
        dto.setWithReservation(order.isWithReservation());
        dto.setPartecipants(order.getPartecipants());
        dto.setTotal(order.getTotal());

        List<OrderDishEntity> orderDishes = order.getOrderDishEntities();
        if (orderDishes != null && !orderDishes.isEmpty()) {
            List<DishDTO> dishDTOList = orderDishes.stream()
                    .map(OrderDishEntity::getDishEntity)
                    .map(dishMapper::fromEntityToDTO)
                    .toList();

            dto.setDishes(dishDTOList);

            BigDecimal baseTotal = dishDTOList.stream()
                    .map(DishDTO::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal surcharge = order.getTotal().subtract(baseTotal);

            dto.setBaseTotal(baseTotal);
            dto.setSurcharge(surcharge);
            dto.setSummary("Base: €" + baseTotal + " + " + order.getDeliveryType().name() + " extra: €" + surcharge);
        } else {
            dto.setDishes(List.of()); // lista vuota
            dto.setBaseTotal(BigDecimal.ZERO);
            dto.setSurcharge(order.getTotal()); // tutto è extra se mancano i piatti
            dto.setSummary("Nessun piatto trovato per questo ordine.");
        }

        return dto;
    }

    private OrderDishEntity buildOrderDish(OrderEntity order, DishEntity dish){
        OrderDishEntity od = new OrderDishEntity();
        od.setOrderEntity(order);
        od.setDishEntity(dish);
        return od;
    }

    public OrderResponseDTO findOrderById(Integer id){
        OrderEntity orderId = orderRepo.findById(id).orElseThrow(()-> new NoSuchElementException("Order not found with id: "  + id));
        return mapToResponse(orderId);
    }

    public List<OrderResponseDTO> findAllOrder(){
        List<OrderEntity> allOrder = orderRepo.findAllWithDishes();
        return allOrder.stream().map(this::mapToResponse).toList();
    }

//    public OrderResponseDTO updateOrder(Integer id, OrderRequestDTO updatedDto) {
//        OrderEntity existingOrder = orderRepo.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Order not found"));
//
//        List<DishEntity> dishes = getAndValidateDishes(updatedDto.getDishListId());
//        BigDecimal total = calculateTotal(updatedDto, dishes);
//
//        existingOrder.setDeliveryType(updatedDto.getDeliveryType());
//        existingOrder.setWithReservation(updatedDto.isWithReservation());
//        existingOrder.setPartecipants(updatedDto.getPartecipants());
//        existingOrder.setTotal(total);
//        existingOrder.setCreationDate(LocalDateTime.now()); // oppure mantenerla fissa
//
//        List<OrderDishEntity> updatedOrderDishes = dishes.stream()
//                .map(d -> {
//                    OrderDishEntity od = new OrderDishEntity();
//                    od.setOrderEntity(existingOrder);
//                    od.setDishEntity(d);
//                    return od;
//                }).toList();
//
//        existingOrder.setOrderDishEntities(updatedOrderDishes);
//
//        orderRepo.save(existingOrder);
//        return mapToResponse(existingOrder);
//    }

    public void deleteOrderById(Integer id) {
        if(!orderRepo.existsById(id)){
            throw  new NoSuchElementException("Order not found with id: " + id);
        }
        dishRepo.deleteById(id);
    }

}
