package com.mainproject.be28.order.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

import com.mainproject.be28.auth.details.PrincipalDetails;
import com.mainproject.be28.auth.utils.UriCreator;
import com.mainproject.be28.member.service.MemberService;
import com.mainproject.be28.order.dto.OrderPageResponseDto;
import com.mainproject.be28.order.dto.OrderPostDto;
import com.mainproject.be28.order.dto.OrderResponseDto;
import com.mainproject.be28.order.entity.Order;
import com.mainproject.be28.order.mapper.OrderMapper;
import com.mainproject.be28.order.service.OrderService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper mapper;


    @PostMapping
    public ResponseEntity<?> createOrderAndProceedToPayment(
            @Valid @RequestBody OrderPostDto orderPostDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            UriComponentsBuilder uriComponentsBuilder) throws IOException {

        Long memberId = principalDetails.getMemberId();
        Order order = orderService.createOrder(orderPostDto, memberId);

        // 주문이 생성되었으므로 해당 주문으로 결제 페이지로 이동하는 URI를 생성합니다.
        URI paymentUri = uriComponentsBuilder.path("/payment/{orderNumber}")
                .buildAndExpand(order.getOrderNumber()).toUri();

        // 결제 페이지로 이동하는 URI와 함께 Created(201) 상태로 응답합니다.
        return ResponseEntity.created(paymentUri).build();
    }


    @GetMapping("/checkout/{order-id}")//특정 주문을 찾고, 해당 주문이 현재 사용자인지 확인
    public ResponseEntity<?> getOrderToPay(@PathVariable("order-id") @PositiveOrZero long orderId,
                                           @RequestParam("memberId") long memberId) { // 결제 창
        Order order = orderService.findOrder(orderId);
        orderService.checkOrderHolder(order, memberId);
        OrderPageResponseDto response = mapper.ordersToOrderPageResponseDto(order);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{memberId}")//현재 사용자의 주문 내역을 조회
    public ResponseEntity<?> getOrderMember(@PathVariable("memberId") long memberId) {
        List<Order> order = orderService.getOrdersByDateToList(memberId);
        List<OrderResponseDto> response = mapper.OrdersToOrderResponseDtos(order);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/{order-number}")
    public ResponseEntity<?> deleteOrder(@PathVariable("order-number") String orderNumber,
                                         @RequestParam("memberId") long memberId) {
        orderService.checkOrderHolder(orderNumber,memberId);
        orderService.cancelOrder(orderNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
   /* @GetMapping("/member/mypage/charge/point")//현재 사용자의 주문 내역을 조회
    public @ResponseBody  void chargePoint(Long amount) {
        int id = orderService.getIdFromAuth();
        orderService.chargePont(new ChargeDto(amount),id);

    }*/

}

