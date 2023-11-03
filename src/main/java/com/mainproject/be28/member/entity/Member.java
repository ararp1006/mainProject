package com.mainproject.be28.member.entity;

import com.mainproject.be28.auditable.Auditable;
import com.mainproject.be28.order.entity.Order;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;


@NoArgsConstructor
@Entity
@AllArgsConstructor
@Getter
@Setter
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String address;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Column
    private String image="";

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20)
    private Status status = Status.MEMBER_ACTIVE;

    @OneToMany(mappedBy = "member")
    private List<Order> order = new ArrayList();




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

    public enum Status {
        MEMBER_ACTIVE("활동중"),
        MEMBER_DELETE("탈퇴 상태");

        private String status;

        Status(String status) {
            this.status = status;}
    }

}
