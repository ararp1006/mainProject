package com.mainproject.be28.order.dto;

import java.util.List;

import com.mainproject.be28.cart.dto.CartItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPostDto { // 장바구니에서 주문으로 넘어갈 때 필요 (Order 엔티티 생성)
    private List<CartItemDto> cartItems;


}
