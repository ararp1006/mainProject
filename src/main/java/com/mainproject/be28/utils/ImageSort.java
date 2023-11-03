package com.mainproject.be28.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ImageSort {
    ITEM_IMAGE("상품 이미지","image/");

    private final String item;
    private final String path;
}