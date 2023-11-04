package com.mainproject.be28.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public interface ExceptionCode {

    HttpStatus getStatus();
    String getMessage();

}