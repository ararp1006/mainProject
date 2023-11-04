package com.mainproject.be28.member.excption;

import com.mainproject.be28.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberException  implements ExceptionCode {
    ONLY_ADMIN_CAN(HttpStatus.NOT_FOUND, "관리자 권한이 필요합니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Member not found"),
    MEMBER_EXISTS(HttpStatus.NOT_FOUND, "Member exists"),
    MEMBER_IS_DELETED(HttpStatus.NOT_FOUND, "Member exists"),
    NO_PERMISSION_EDITING_POST(HttpStatus.NOT_FOUND, "Member not authorized"),
    MEMBER_NOT_AUTHORIZED(HttpStatus.NOT_FOUND, "Member not authorized"),
    JWT_TOKEN_EXPIRED(HttpStatus.NOT_FOUND, "Member not authorized"),
    VERIFY_FAILURE(HttpStatus.NOT_FOUND, "Member not authorized");

    private final HttpStatus status;
    private final String message;
}
