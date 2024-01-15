package com.mainproject.be28.auth.exception;

import com.mainproject.be28.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtException implements ExceptionCode {
    JWT_TOKEN_EXPIRED(HttpStatus.INTERNAL_SERVER_ERROR,"토큰이 만료되었습니다.");

    private final HttpStatus status;
    private final String message;
}