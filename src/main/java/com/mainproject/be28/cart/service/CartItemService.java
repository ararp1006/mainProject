package com.mainproject.be28.cart.service;

import com.mainproject.be28.cart.entity.Cart;
import com.mainproject.be28.cart.entity.CartItem;
import com.mainproject.be28.cart.exception.CartException;
import com.mainproject.be28.cart.repository.CartItemRepository;
import com.mainproject.be28.exception.BusinessLogicException;
import com.mainproject.be28.item.entity.Item;
import com.mainproject.be28.item.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ItemService itemService;

    public CartItemService(CartItemRepository cartItemRepository, ItemService itemService) {
        this.cartItemRepository = cartItemRepository;
        this.itemService = itemService;
    }
    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }
    public void changeQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new BusinessLogicException(CartException.CART_NOT_FOUND));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public void plusQuantity(CartItem cartItem, int quantity){
        cartItem.plusQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

}
