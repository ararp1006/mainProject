package com.mainproject.be28.member.dto;

import com.mainproject.be28.image.entity.MemberImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginDto {

    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 2, max = 20)
    private String name;

    private String profileImg;

}