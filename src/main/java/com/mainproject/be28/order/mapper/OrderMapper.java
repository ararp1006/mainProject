package com.mainproject.be28.order.mapper;

import com.mainproject.be28.order.dto.OrderPageResponseDto;
import com.mainproject.be28.order.dto.OrderPostDto;
import com.mainproject.be28.order.dto.OrderResponseDto;
import com.mainproject.be28.order.entity.Order;

import com.mainproject.be28.order.dto.OrderItemResponseDto;
import com.mainproject.be28.order.entity.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    default OrderPageResponseDto ordersToOrderPageResponseDto(Order order) {
        OrderPageResponseDto orderPageResponseDto = new OrderPageResponseDto();
        orderPageResponseDto.setOrderNumber(order.getOrderNumber());
        orderPageResponseDto.setAddress(String.valueOf(order.getMember().getAddress()));
        orderPageResponseDto.setName(order.getMember().getName());
        orderPageResponseDto.setPhone(order.getMember().getPhoneNumber());
        orderPageResponseDto.setTotalPrice(order.getTotalPrice());
        orderPageResponseDto.setEmail(order.getMember().getEmail());
        return orderPageResponseDto;
    }
    default List<OrderResponseDto> OrdersToOrderResponseDtos(List<Order> orders) {
        if (orders == null) return null;
        // 주문 목록을 스트림으로 변환하고, 각 주문을 OrderResponseDto로 변환하는 람다 표현식을 사용하여 매핑
        List<OrderResponseDto> orderResponseDtos = orders.stream().map(order -> {
            OrderResponseDto orderResponseDto = new OrderResponseDto();
            orderResponseDto.setName(order.getMember().getName());
            orderResponseDto.setOrderNumber(order.getOrderNumber());
            orderResponseDto.setCreatedAt(order.getCreatedAt());
            orderResponseDto.setOrderStatus(order.getStatus().getStatus());
            orderResponseDto.setTotalPrice(order.getTotalPrice());

            // 주문 상품 목록을 스트림으로 변환하고, 각 주문 상품을 OrderItemResponseDto로 변환
            List<OrderItem> orderItems = order.getOrderItems();
            List<OrderItemResponseDto> orderItemResponseList = orderItems.stream().map(orderItem -> {
                OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
                orderItemResponseDto.setName(orderItem.getItem().getName());
                orderItemResponseDto.setPrice((long) orderItem.getItem().getPrice());
                orderItemResponseDto.setQuantity(orderItem.getQuantity());
                return orderItemResponseDto;
            }).collect(Collectors.toList());

            orderResponseDto.setItems(orderItemResponseList);
            return orderResponseDto;
        }).collect(Collectors.toList());

        return orderResponseDtos;
    }
}






