package com.mainproject.be28.order.service;


import com.mainproject.be28.cart.dto.CartItemDto;
import com.mainproject.be28.cart.entity.CartItem;
import com.mainproject.be28.cart.exception.CartException;
import com.mainproject.be28.cart.repository.CartItemRepository;
import com.mainproject.be28.exception.BusinessLogicException;
import com.mainproject.be28.item.entity.Item;
import com.mainproject.be28.item.repository.ItemRepository;
import com.mainproject.be28.item.service.ItemService;
import com.mainproject.be28.member.entity.Member;
import com.mainproject.be28.member.repository.MemberRepository;
import com.mainproject.be28.member.service.MemberService;
import com.mainproject.be28.order.data.OrderData;
import com.mainproject.be28.order.data.OrderStatus;
import com.mainproject.be28.order.dto.OrderPatchStatusDto;
import com.mainproject.be28.order.dto.OrderPostDto;
import com.mainproject.be28.order.entity.Order;
import com.mainproject.be28.order.exception.OrderException;
import com.mainproject.be28.order.repository.OrderRepository;
import com.mainproject.be28.order.dto.OrderItemPostDto;
import com.mainproject.be28.order.entity.OrderItem;
import com.mainproject.be28.order.repository.OrderItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemService itemService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

   //주문생성
    public Order createOrder(OrderPostDto orderPostDto, Long memberId) throws IOException {
        Order order = new Order();
        order.makeOrderNumber();
        Member member = memberService.getMember(memberId);
        memberService.checkActive(member);
        order.addMember(member);
        // 주문 아이템 처리
        cartItemPostDtoToOrdersItem(orderPostDto.getCartItems(), order);

        // 주문 정보 설정 (주문 번호, 주문 총액, 주문 상태, 주소, 등등...)


        // 주문을 저장
        return orderRepository.save(order);
    }


    ////카트아이템을 오더아이템으로 바꿈
    private void cartItemPostDtoToOrdersItem(List<CartItemDto> cartItemDtos, Order order) {
        List<OrderItem> orderItems = new ArrayList<>(); // cartItems 빈 리스트 생성

        cartItemDtos.forEach(cartItemDto -> {
            Item item = itemService.findItem(cartItemDto.getItems().get(0).getItemId());
            long quantity = cartItemDto.getItems().get(1).getQuantity();

            OrderItem orderItem = new OrderItem(quantity, item);
            orderItem.addOrder(order);
            orderItem.setName(item.getName());
            orderItem.setPrice(item.getPrice());

            orderItems.add(orderItem);
        });


        order.getOrderItems().addAll(orderItems);
        long totalPrice = getTotalPrice(orderItems);
        order.setTotalPrice(totalPrice);

        // 주문 아이템 저장
        orderItemRepository.saveAll(orderItems);
    }


    //총합 가격
    public long getTotalPrice(List<OrderItem> orderItems) {
        long price = 0;
        for(OrderItem orderItem : orderItems){
            price += orderItem.getPrice() * orderItem.getQuantity();
        }
        return price;
    }

    //주문번호로 주문찾기
    public Order findByOrderNumber(String orderNumber) {
        Optional<Order> order = orderRepository.findByOrderNumber(orderNumber);
        return order.orElseThrow(() -> new BusinessLogicException(OrderException.ORDER_NOT_FOUND));
    }
    //주문아이디로 주문찾기
    public Order findOrder(long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.orElseThrow(() -> new BusinessLogicException(OrderException.ORDER_NOT_FOUND));
    }
    // 로그인한 유저가 주문자인지 확인
    public void checkOrderHolder(Order order, long memberId) {
        if (!order.getMember().isSameMemberId(memberId)) {
            throw new BusinessLogicException(OrderException.NOT_ORDER_HOLDER);
        }
    }
    //주문내역 확인
    public List<Order> getOrdersByDateToList(long memberId) {
        Member member = memberService.findVerifiedMember(memberId);
        return orderRepository.findByMemberOrderByCreatedAtDesc(member);// creat_At을 기준으로 내림차순으로
    }
    //주문번호 확인
    public void checkOrderHolder(String orderNumber, Long memberId) {
        long orderHolderId = findByOrderNumber(orderNumber).getMember().getMemberId();
        if(orderHolderId != memberId) {
            throw new BusinessLogicException(OrderException.NOT_ORDER_HOLDER);
        }
    }
    //주문취소
    public void cancelOrder(String orderNumber) {
        Order order = findByOrderNumber(orderNumber);
        int index = order.getStatus().getIndex();
        if (index == OrderStatus.DELIVERY_COMPLETED.getIndex()
                || index == OrderStatus.DELIVERY_IN_PROGRESS.getIndex()) {
            order.applyRefund();
        } else if (index == 1) {
            // 주문 취소 메서드 필요 (아임포트)
            order.cancelOrder();
        } else if (index == 5) {
            throw new BusinessLogicException(OrderException.ALREADY_APPLIED_REFUND);
        } else if (index == 2) {
            throw new BusinessLogicException(OrderException.ALREADY_CANCELED);
        } else {
            orderRepository.delete(order);
        }
    }
    //주문 처리 중에 오류가 발생한 경우
    public void errorWhilePaying(Order order) {
        order.errorWhilePaying();
        orderRepository.save(order);
    }

    //주문 결제
    public void paidOrder(Order order) {
        order.paid();
        orderRepository.save(order);
    }
    //주문상태 변경
    public void changeStatus(String orderNumber, OrderPatchStatusDto dto) {
        Order order = findByOrderNumber(orderNumber);
        order.setStatus(OrderStatus.valueOfStatus(dto.getStatus()));
        orderRepository.save(order);
    }

    // 장바구니 상품(들) 주문
    public Long orders(Order order, List<OrderItemPostDto> orderDtoList, Long memberId) {
        // 로그인한 유저 조회
        Member member = memberService.findVerifiedMember(memberId);

        // 주문 항목 리스트 생성
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderItemPostDto cartOrderDto : orderDtoList) {
            Item item = itemRepository.findById(cartOrderDto.getItemId())
                    .orElseThrow(() -> new BusinessLogicException(CartException.CART_NOT_FOUND));
            OrderItem orderItem = OrderItem.createOrderItem(item, cartOrderDto.getQuantity());
            orderItemList.add(orderItem);
        }
        //Order Entity 클래스에 존재하는 createOrder 메소드로 Order 생성 및 저장
        order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);
        return order.getOrderId();
    }

  /*  public Point chargePoint(PointChargeDto dto, int id){
        Member member = memberRepository.findMemberById(id);
        dto.setMember(member);
        return pointRepository.save(mapper.map(dto,Point.class))
    }*/



}
