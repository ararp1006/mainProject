package com.mainproject.be28.member.dto;


import com.mainproject.be28.member.data.Address;
import com.mainproject.be28.member.data.MemberStatus;
import com.mainproject.be28.member.entity.Member;
import com.mainproject.be28.order.data.DeliveryInformation;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class MemberDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class PostDto{
        @NotBlank
        private String name;
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String password;


    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatchDto{
        private String name;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {

        private long memberId;
        private String email;
        private String name;
        private String phoneNumber;
        private Address address;
        private DeliveryInformation deliveryInformation;
        private String imagePath;
        private MemberStatus status;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
    }
}