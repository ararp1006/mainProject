package com.mainproject.be28.cart.dto;

import com.mainproject.be28.item.dto.ItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.*;
public class CartDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post {
        private long cartItemId;
    }

    @Getter
        public class Patch {
            @Positive
            private Long cartItemId;
            @Positive
            @Max(100)
            @Min(1)
            private int quantity;
        }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private List<CartItemResponseDto> cartItems;
        private long totalPrice;

    }



}
