package com.mainproject.be28.cart.service;

import com.mainproject.be28.cart.dto.CartItemDto;
import com.mainproject.be28.cart.entity.Cart;
import com.mainproject.be28.cart.exception.CartException;
import com.mainproject.be28.cart.repository.CartRepository;
import com.mainproject.be28.cart.entity.CartItem;
import com.mainproject.be28.cart.repository.CartItemRepository;
import com.mainproject.be28.exception.BusinessLogicException;
import com.mainproject.be28.item.entity.Item;
import com.mainproject.be28.item.exception.ItemException;
import com.mainproject.be28.item.service.ItemService;
import com.mainproject.be28.member.service.MemberService;
import com.mainproject.be28.order.dto.CartOrderDto;
import com.mainproject.be28.order.entity.Order;
import com.mainproject.be28.order.service.OrderService;
import com.mainproject.be28.orderItem.dto.OrderItemPostDto;
import com.mainproject.be28.utils.CustomBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartService {
    private final MemberService memberService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemService cartItemService;
    private final ItemService itemService;
    private final CustomBeanUtils<Cart> beanUtils;
    private final OrderService orderService;
    private Map<Long, CartItem> items; // 카트에 담긴 아이템들을 관리하는 맵

    public CartService(MemberService memberService, ItemService itemService, OrderService orderService, CartItemService cartItemService,
                       CartRepository cartRepository, CartItemRepository cartItemRepository,
                       CustomBeanUtils<Cart> beanUtils) {
        this.memberService = memberService;
        this.itemService = itemService;
        this.orderService = orderService;
        this.cartItemService = cartItemService;

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;

        this.beanUtils = beanUtils;

    }
    public void createCart(Cart cart, Long cartItemId){
        addCartItem(cart, cartItemId, 1);

        cart.calculateTotalPrice();
        cartRepository.save(cart);
    }

    private void addCartItem(Cart cart,  Long itemId, int quantity) {
        CartItem findCartItem = findSameCartItemInCart(cart, itemId); //카트찾기
        if(findCartItem!=null) {
            cartItemService.plusQuantity(findCartItem, quantity);//카트가 있다면 수량 증가
        }
        else{ //카트가 없다면 새로 카트아이템 저장
            Item item = itemService.findItem(itemId);
            if (item == null) {
                throw new BusinessLogicException(CartException.CART_NOT_FOUND);
            }
            CartItem cartItem = new CartItem(item, quantity);
            cartItemService.saveCartItem(cartItem);
            cart.addCartItem(cartItem);
        }
    }

    //카트에 이미 있는 카트아이템인지 조사하기 -> 리턴 : 해당 카트아이템
    private CartItem findSameCartItemInCart(Cart cart, Long itemId){
        List<CartItem> cartItemList = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getItemId() == itemId)
                .collect(Collectors.toList());

        if(cartItemList.size()==1){
            return cartItemList.get(0);
        }
        return null;
    }


    public void deleteCart(long cartItemId) { // 장바구니 내 개별 상품 제거
        CartItem cartItem = cartItemRepository.findByCartItemId(cartItemId);
        cartItemRepository.delete(cartItem);
    }

    public void changeCartQuantity(Cart cart, Long cartItemId, int quantity) {
        verifyExistsCartItemIsInCart(cart, cartItemId);

        cartItemService.changeQuantity(cartItemId, quantity);

        cart.calculateTotalPrice();
        cartRepository.save(cart);
    }

    private void verifyExistsCartItemIsInCart(Cart cart, Long cartItemId) {
        boolean isInCart = cart.getCartItems().stream()
                .anyMatch(cartItem->cartItem.getCartItemId()==cartItemId);
        if(isInCart == false){
            throw new BusinessLogicException(CartException.CARTMEALBOX_NOT_IN_CART);
        }
    }
    // 장바구니 상품(들) 주문
    public Long orderCartItem(Order order, List<CartOrderDto> cartOrderDtoList, Long memberId) {
        List<OrderItemPostDto> orderDtoList = new ArrayList<>();

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(() -> new BusinessLogicException(CartException.CART_NOT_FOUND));

            // CartOrderDto를 OrderItemPostDto로 변환
            OrderItemPostDto orderDto = new OrderItemPostDto();
            orderDto.setItemId(cartItem.getItem().getItemId());
            orderDto.setQuantity(cartItem.getQuantity());

            // OrderItemPostDto를 리스트에 추가
            orderDtoList.add(orderDto);
        }

        // 주문 생성 메서드 호출
        Long orderId = orderService.orders(order, orderDtoList, memberId);

        // 주문한 장바구니 상품을 제거
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(() -> new BusinessLogicException(CartException.CART_NOT_FOUND));
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }


}
