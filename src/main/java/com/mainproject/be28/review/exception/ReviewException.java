package com.mainproject.be28.review.exception;

import com.mainproject.be28.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewException  implements ExceptionCode {
    Review_NOT_FOUND(HttpStatus.NOT_FOUND,"REVIEW not found" );

    private final HttpStatus status;
    private final String message;
}
