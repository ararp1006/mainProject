package com.mainproject.be28.image.entity;


import com.mainproject.be28.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_IMAGE_ID")
    private Long id;

    @Embedded
    private ImageInfo imageInfo;

    /* ####### JPA 매핑 ####### */

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;

}