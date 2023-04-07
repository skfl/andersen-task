package com.andersentask.bookshop.order.mappers;

import com.andersentask.bookshop.order.dtos.OrderDTO;
import com.andersentask.bookshop.order.entities.Order;

import java.util.List;

public class OrderMapper {


    // update to order DTO (now there will be compilation error)
    public static OrderDTO entityToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .user(order.getUser())
                .orderCost(order.getOrderCost())
                .orderStatus(order.getOrderStatus())
                .timeOfCompletingOrder(order.getTimeOfCompletingOrder())
                .booksInOrder(BookMapper.entityListToDtoList(order.getBooksInOrder()))
                .build();
    }


    // update to order DTO (now there will be compilation error)
    public static Order dtoToEntity(OrderDTO orderDTO) {
        return Order.builder()
                .id(orderDTO.getId())
                .user(orderDTO.getUser())
                .orderCost(orderDTO.getOrderCost())
                .orderStatus(orderDTO.getOrderStatus())
                .timeOfCompletingOrder(orderDTO.getTimeOfCompletingOrder())
                .booksInOrder(BookMapper.dtoListToEntityList(orderDTO.getBooksInOrder()))
                .build();
    }

    public static List<OrderDTO> entityListToDtoList(List<Order> orderList) {
        return orderList.stream()
                .map(OrderMapper::entityToDTO)
                .toList();
    }

    public static List<Order> dtoListToEntityList(List<OrderDTO> orderDTOList){
        return orderDTOList.stream()
                .map(OrderMapper::dtoToEntity)
                .toList();
    }

}
