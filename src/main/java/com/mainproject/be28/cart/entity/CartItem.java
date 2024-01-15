package com.mainproject.be28.cart.entity;

import com.mainproject.be28.auditable.Auditable;
import com.mainproject.be28.item.entity.Item;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends Auditable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column()
        private Long cartItemId;

        @Column(nullable = false)
        private int quantity;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "CART_ID", nullable = false)
        private Cart cart;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "ITEM_ID", nullable = false)
        private Item item;

        public CartItem(Item item, int quantity) {
                this.item = item;
                this.quantity = quantity;
        }
        public void plusQuantity(int quantity){
                this.quantity = this.getQuantity() + quantity;
        }
}
