package com.mainproject.be28.cart.controller;

import com.mainproject.be28.auth.details.PrincipalDetails;
import com.mainproject.be28.cart.dto.CartDto;
import com.mainproject.be28.cart.entity.Cart;
import com.mainproject.be28.cart.entity.CartItem;
import com.mainproject.be28.cart.mapper.CartMapper;
import com.mainproject.be28.cart.service.CartService;
import com.mainproject.be28.cart.dto.CartItemDto;
import com.mainproject.be28.item.entity.Item;
import com.mainproject.be28.item.service.ItemService;
import com.mainproject.be28.member.service.MemberService;
import com.mainproject.be28.order.dto.CartOrderDto;
import com.mainproject.be28.order.entity.Order;
import com.mainproject.be28.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member/cart")
@Validated
public class CartController {

    private final CartService cartService;
    private final CartMapper mapper;
    private final MemberService memberService;


    @PostMapping
    public ResponseEntity addCart(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @RequestBody @Valid CartDto.Post cartPostDto){
        log.info("------AddCart------");
        Cart cart = memberService.getMember(principalDetails.getMemberId()).getCart();
        cartService.createCart(cart, cartPostDto.getCartItemId());
        return new ResponseEntity(HttpStatus.CREATED);
    }
    /*//비로그인 -> 로그인 했을 때 장바구니에 전부 넣기
    @PostMapping("/all")
    public ResponseEntity addAllToCartWhenLogin(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @RequestBody @Valid CartDto.All cartAllPostDto) {
        log.info("------addAllToCartWhenLogin------");
        Cart cart = memberService.getMember(principalDetails.getMemberId()).getCart();
        List<CartAllPostDto.CustomMealboxDto> customMealboxDtos = cartAllPostDto.getCustomMealboxes();
        List<CartAllPostDto.AdminMadeMealboxDto> adminMadeMealboxDtos = cartAllPostDto.getAdminMadeMealboxes();

        cartService.addAllMealboxes(cart, customMealboxDtos, adminMadeMealboxDtos);
        log.info(cart.toString());
        return new ResponseEntity(HttpStatus.CREATED);
    }*/
    @PatchMapping
    public ResponseEntity changeCartItemCount(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             @RequestBody @Valid CartDto.Patch cartPatchDto){
        log.info("------ChangeCartItemCount------");
        Cart cart = memberService.getMember(principalDetails.getMemberId()).getCart();
        cartService.changeCartQuantity(cart, cartPatchDto.getCartItemId(), cartPatchDto.getQuantity());
        return new ResponseEntity(HttpStatus.OK);
    }

    //장바구니 화면 띄워주기
    @GetMapping
    public ResponseEntity getCart(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("------getCart------");
        Cart cart = memberService.getMember(principalDetails.getMemberId()).getCart();
        CartDto.Response cartResponseDto = mapper.cartToCartResponseDto(cart);
        return new ResponseEntity(new SingleResponseDto(cartResponseDto),HttpStatus.OK);
    }
    //카트아이템 삭제
    @DeleteMapping("/delete/{cartItemId}")
    public SingleResponseDto deleteCartItem(@PathVariable("cartItemId") long cartItemId) {
        cartService.deleteCart(cartItemId);
        return new SingleResponseDto<>(HttpStatus.NO_CONTENT);
    }


    // 장바구니 상품(들) 주문
    @PostMapping(value = "/orders/{memberId}")
    @ResponseBody
    public ResponseEntity orders(@RequestBody CartOrderDto cartOrderDto, @PathVariable("memberId") @Positive Long memberId ) {

    List<CartOrderDto> orderDtoList = cartOrderDto.getCartOrderItemList();

        if (orderDtoList == null || orderDtoList.size() == 0) {
            return new ResponseEntity<String>("주문할 상품을 선택해주세요.", HttpStatus.BAD_REQUEST);
        }
        Order order = new Order();
        Long orderId = cartService.orderCartItem(order,orderDtoList, memberId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}
