package com.mainproject.be28.auth.dto;

import lombok.*;

@Getter
@Setter
@ToString
public class PrincipalDto {

    private Long id;
    private String email;
    private String name;
    @Builder
    public PrincipalDto(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}