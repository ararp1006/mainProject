package com.mainproject.be28.item.entity;

import com.mainproject.be28.auditable.Auditable;
import com.mainproject.be28.cart.entity.CartItem;
import com.mainproject.be28.image.entity.ItemImage;
import com.mainproject.be28.review.entity.Review;
import lombok.*;

import java.util.*;
import java.util.List;
import javax.persistence.*;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(length = 100)
    private String name;

    @Column
    private int price;

    @Column(length = 100)
    private String detail;

    @Column(length = 100)
    private String status;

    @Column(length = 100)
    private String color;

    @Column(length = 100)
    private String brand;

    @Column(length = 100)
    private String category;

    @Transient
    private Double score;

    @Transient
    private long reviewCount;

    @Column(length = 100)
    private Integer stock;

    @Column(nullable = false)
    @Builder.Default
    private boolean forSale = true;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ItemImage> itemImage = new ArrayList<>();



    public void patchItem(Item itemPatcher){
        this.name = itemPatcher.getName();
        this.color = itemPatcher.getColor();
        this.price = itemPatcher.getPrice();
        this.brand = itemPatcher.getBrand();

    }

}
