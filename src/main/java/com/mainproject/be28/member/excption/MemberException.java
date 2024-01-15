package com.mainproject.be28.member.excption;

import com.mainproject.be28.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberException  implements ExceptionCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER Not Found"),
    MEMBER_EXIST(HttpStatus.CONFLICT, "MEMBER is already Exist!"),
    MAILKEY_MISMATCH(HttpStatus.CONFLICT, "Incorrect password."),
    INCORRECT_PASSWORD(HttpStatus.CONFLICT, "Password"),
    NOT_YET_AUTHENTICATE_EMAIL(HttpStatus.FORBIDDEN, "Do Not Authenticate Email Yet"),
    NOT_ACTIVE_MEMBER(HttpStatus.FORBIDDEN, "This MEMBER Is Not Active"),
    NOT_GOOGLE_MEMBER(HttpStatus.CONFLICT, "Not Google MEMBER"),
    NO_PERMISSION_EDITING_POST(HttpStatus.FORBIDDEN, "This MEMBER Is Not Active"),
    NO_AUTHENTICATE_MEMEBER(HttpStatus.FORBIDDEN, "인증정보가 없습니다."),
    NO_PRINCIPAL_MEMEBER(HttpStatus.FORBIDDEN, "멤버인증정보가 없습니다.."),
    GOOGLE_MEMBER(HttpStatus.NOT_FOUND, "Google MEMBER");

    private final HttpStatus status;
    private final String message;
}
