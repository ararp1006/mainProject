package com.mainproject.be28.auth.dto;

import lombok.*;

import java.security.Principal;

@Getter
@Setter
@ToString
public class PrincipalDto implements Principal {

    private Long id;
    private String email;
    private String name;
    @Builder
    public PrincipalDto(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
    @Override
    public String getName() {

        return this.email;
    }
}