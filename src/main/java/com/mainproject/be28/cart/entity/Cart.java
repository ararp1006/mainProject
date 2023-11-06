package com.mainproject.be28.cart.entity;

import com.mainproject.be28.auditable.Auditable;
import com.mainproject.be28.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends Auditable  {
    @Id
    @Column(name = "CART_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId;

    @Column(nullable = false)
    @Builder.Default
    @Setter
    private int totalPrice = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_Id")
    private Member member;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems = new ArrayList<>();

    public void addCartItem(CartItem cartItem) {
        if (cartItem != null) {
            this.cartItems.add(cartItem);
        }
    }
    public void calculateTotalPrice() {
        this.totalPrice = this.getCartItems().stream().mapToInt(cartItem ->
                        cartItem.getQuantity() * cartItem.getItem().getPrice())
                .sum();
    }


}
