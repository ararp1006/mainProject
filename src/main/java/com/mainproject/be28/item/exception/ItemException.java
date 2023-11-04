package com.mainproject.be28.item.exception;

import com.mainproject.be28.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum  ItemException  implements ExceptionCode {
    ITEM_EXIST(HttpStatus.NOT_FOUND, "Item_Exist"),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "Item_Not_FOUND");
    private final HttpStatus status;
    private final String message;
}
