package com.mainproject.be28.cart.mapper;

import com.mainproject.be28.cart.dto.CartDto;
import com.mainproject.be28.cart.entity.Cart;
import com.mainproject.be28.cart.service.CartService;
import com.mainproject.be28.cart.dto.CartItemResponseDto;
import com.mainproject.be28.cart.entity.CartItem;
import com.mainproject.be28.cart.repository.CartItemRepository;
import com.mainproject.be28.member.service.MemberService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
// 빈 카트 조회 시 NullPointException 해결필요
@Component
public class CartMapperImpl implements CartMapper{
    public final CartItemRepository cartItemRepository;
    public final MemberService memberService;
    public final CartService cartService;

    public CartMapperImpl(CartItemRepository cartItemRepository, MemberService memberService, CartService cartService) {
        this.cartItemRepository = cartItemRepository;
        this.memberService = memberService;
        this.cartService = cartService;
    }

    public CartDto.Response cartToCartResponseDto(Cart cart) {

        List<CartItemResponseDto> cartItemResponseDtos = getCartItemsResponseDto(cart);
        long price = getTotalPrice(cartItemResponseDtos);
        return new CartDto.Response(cartItemResponseDtos, price);
    }

    public long getTotalPrice(List<CartItemResponseDto> cartItemResponseDtos) {
        long price = 0;
        for(CartItemResponseDto cartItemResponseDto : cartItemResponseDtos){
            price += cartItemResponseDto.getPrice()*cartItemResponseDto.getCount();
        }
        return price;
    }


    public List<CartItemResponseDto> getCartItemsResponseDto(Cart cart) {
        List<CartItemResponseDto> cartItemDtos = new ArrayList<>();
        if (cart == null) { return new ArrayList<>();}
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems != null) {
            for (CartItem cartItem : cartItems) {
                if (cartItem.getQuantity()!= 0) {
                    CartItemResponseDto cartItemDto =
                            CartItemResponseDto.builder()
                                    .cartItemId(cartItem.getCartItemId())
                                    .itemId(cartItem.getItem().getItemId())
                                    .count((long) cartItem.getQuantity())
                                    .name(cartItem.getItem().getName())
                                    .price(cartItem.getItem().getPrice())
                                    .build();
                    cartItemDtos.add(cartItemDto);
                }
            }
        }
        return cartItemDtos;
    }
}
