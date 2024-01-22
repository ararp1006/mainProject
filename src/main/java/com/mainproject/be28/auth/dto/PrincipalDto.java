package com.mainproject.be28.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.security.Principal;

@Getter
@Setter
@ToString
public class PrincipalDto implements Principal {

    private Long id;
    private String email;
    private String name;
    private String roles;

    @Builder
    public PrincipalDto(Long id, String email, String name, String roles) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.roles = roles;
    }

    @Override
    public String getName() {
        return this.email;
    }

    public String getUserName() {
        return this.name;
    }
    }
