package com.mainproject.be28.response;

import com.mainproject.be28.exception.ExceptionCode;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String status;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ExceptionCode exceptionCode) {
        return ResponseEntity
                .status(exceptionCode.getStatus())
                .body(ErrorResponse.builder()
                        .message(exceptionCode.getMessage())
                        .status(exceptionCode.getStatus().toString())
                        .build()
                );
    }

    public static ErrorResponse of(HttpStatus httpStatus) {
        return ErrorResponse.builder()
                .message(httpStatus.getReasonPhrase())
                .status(httpStatus.toString())
                .build()
                ;
    }
}