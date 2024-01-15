package com.mainproject.be28.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class DataResponseDto<T> {
    private T data;
}
