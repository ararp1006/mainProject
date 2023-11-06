package com.mainproject.be28.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Data
@AllArgsConstructor
@Getter
public class Recovery {
    @Data
    @AllArgsConstructor
    @Getter
    public class RecoveryEmailSendDto {

        @NotNull
        @Email
        private String emailSignUp;
        @NotNull
        @Email
        private String emailNeedToSend;

    }
    @Data
    @AllArgsConstructor
    @Getter
    public class RecoveryPWEmailSendDto {
        @NotNull
        @Email
        private String email;

    }
    @Data
    @AllArgsConstructor
    @Getter
    public class RecoveryPasswordPatchDto {
        @NotNull
        @Email
        private String email;
        @NotNull
        private String mailKey;
        @NotNull
        @Pattern(regexp = "(?=.*\\d{1,50})(?=.*[a-zA-Z]{1,50}).{8,20}$")
        private String afterPassword;
    }
}
