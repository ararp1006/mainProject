package com.mainproject.be28.member.dto;


import com.mainproject.be28.member.data.Address;
import com.mainproject.be28.member.data.MemberStatus;
import com.mainproject.be28.order.data.DeliveryInformation;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.*;

import java.time.LocalDateTime;

public class MemberDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Builder
    public static class PostDto{
        @NotNull
        @Email
        private String email;
        @NotNull
        //@Pattern(regexp = "(?=.*\\d{1,50})(?=.*[a-zA-Z]{1,50}).{8,20}$")
        private String password;
        @NotNull
        @Size(min = 2, max = 10)
        private String name;


    }
    @Data
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PatchDto {
        @NotNull
        @Size(min = 2, max = 10)
        private String name;
        @Pattern(regexp = "01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$")
        private String phoneNumber;
        private Address address;
        private DeliveryInformation deliveryInformation;
    }

    @Data
    @AllArgsConstructor
    public static class ResponseDto {

        private Long id;
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