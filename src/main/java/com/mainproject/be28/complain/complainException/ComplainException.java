package com.mainproject.be28.complain.complainException;

import com.mainproject.be28.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum  ComplainException implements ExceptionCode {
    Complain_NOT_FOUND(HttpStatus.NOT_FOUND,"COMPLAIN not found");
    private final HttpStatus status;
    private final String message;
}
