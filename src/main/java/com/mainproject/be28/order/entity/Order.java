package com.mainproject.be28.order.entity;

import com.mainproject.be28.auditable.Auditable;
import com.mainproject.be28.member.entity.Member;
import com.mainproject.be28.order.data.OrderStatus;
import com.mainproject.be28.order.entity.OrderItem;
import com.mainproject.be28.payment.entity.PayInfo;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table (name = "orders")
public class Order extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false, unique = true)
    private String orderNumber; // 주문 번호

    private Long totalPrice; // 주문 총액

    @Builder.Default
    private OrderStatus status = OrderStatus.NOT_PAID; // 주문 상태

    private String addressee; // 받는 사람

    private String zipCode; // 배송 우편번호

    private String simpleAddress; // 배송 주소

    private String detailAddress; // 배송 상세주소

    private String phoneNumber; // 수령인 전화번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Member_ID")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order")
    private PayInfo payInfo;





    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        if (orderItem.getOrder() != this) {
            orderItem.setOrder(this);
        }
    }
    public void addPayInfo(PayInfo payInfo) {
        this.payInfo = payInfo;
        if(payInfo.getOrder() != this) {
            payInfo.addOrder(this);
        }
    }
    public void addMember(Member member) {
        this.member = member;
        if (!member.getOrder().contains(this)) {
            member.addOrder(this);
        }
    }
    //환불
    public void applyRefund() {
        this.status = OrderStatus.REFUND_APPLIED;
    }
    //주문완료
    public void paid() {
        this.status = OrderStatus.ORDER_COMPLETED;
    }
    //주문취소
    public void cancelOrder() {
        this.status = OrderStatus.ORDER_CANCELED;
    }


    public void errorWhilePaying() {
        this.status = OrderStatus.PAYMENT_AMOUNT_WRONG;
    }

    public void makeOrderNumber() {
        String date = ZonedDateTime.now(ZoneId.of("UTC")).plusHours(9).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        this.orderNumber = date + RandomStringUtils.randomNumeric(6);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {

        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.NOT_PAID);
        return order;
    }


}
