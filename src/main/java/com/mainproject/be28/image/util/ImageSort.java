package com.mainproject.be28.image.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ImageSort {
    Item_IMAGE("상품 이미지","item/"),
    Member_IMAGE("회원 이미지","member/");

    private final String item;
    private final String path;
}