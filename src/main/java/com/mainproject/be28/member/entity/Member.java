package com.mainproject.be28.member.entity;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PROTECTED;

import com.mainproject.be28.auditable.Auditable;
import com.mainproject.be28.cart.entity.Cart;
import com.mainproject.be28.image.entity.MemberImage;
import com.mainproject.be28.member.data.Address;
import com.mainproject.be28.member.data.MemberStatus;
import com.mainproject.be28.order.data.DeliveryInformation;
import com.mainproject.be28.order.entity.Order;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.*;
import lombok.Builder.Default;


@Entity
@Getter
@Builder
@Table
@Setter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Setter
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    @Setter
    private String email;

    @Column(nullable = false)
    @Setter
    private String name;

    @Column(unique = true)
    @Setter
    private String phoneNumber;

    @Setter
    @Embedded
    @Default
    private Address address = new Address();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private MemberImage memberImage;

    @ElementCollection(fetch = EAGER)
    @Setter
    @Builder.Default
    private List<String> roles = new ArrayList<>();


    @Enumerated(STRING)
    @Default
    @Setter
    private MemberStatus status = MemberStatus.MEMBER_TMP;

    //이메일 인증이 되었는지 확인하는 키
    @Default
    @Setter
    private String mailKey = "";

    @Setter
    @Embedded
    @Default
    @AttributeOverride(name = "name", column = @Column(name = "addressee"))
    @AttributeOverride(name = "phoneNumber", column = @Column(name = "addresseePhoneNumber"))
    @AttributeOverride(name = "address.zipCode", column = @Column(name = "deliveryZipCode"))
    @AttributeOverride(name = "address.simpleAddress", column = @Column(name = "deliverySimpleAddress"))
    @AttributeOverride(name = "address.detailAddress", column = @Column(name = "deliveryDetailsAddress"))
    private DeliveryInformation deliveryInformation = new DeliveryInformation();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private Cart cart;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Order> order = new ArrayList();


    public Member(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.name = member.getName();
        this.memberImage = member.getMemberImage();
        this.roles = new ArrayList<>(member.getRoles()); // 깊은 복사를 하기 위해 새 ArrayList를 생성
    }




    public void addOrder(Order order) {
        this.order.add(order);
        if (order.getMember() != this) {
            order.addMember(this);
        }
    }
    public boolean isSameMemberId(long memberId){
           return this.memberId == memberId;
        }

    public Member(String email) {
        this.email = email;
    }



}
