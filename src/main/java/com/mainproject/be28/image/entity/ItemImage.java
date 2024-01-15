package com.mainproject.be28.image.entity;


import com.mainproject.be28.auditable.Auditable;
import com.mainproject.be28.item.entity.Item;
import lombok.*;

import javax.persistence.*;


@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class ItemImage extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_IMAGE_ID")
    private Long id;

    @Embedded
    private ImageInfo imageInfo;


    /* ####### JPA 매핑 ####### */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    @Setter
    private Item item;


}