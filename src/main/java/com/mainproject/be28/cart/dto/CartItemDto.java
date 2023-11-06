package com.mainproject.be28.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CartItemDto {
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Size(min = 1, max = 15)
    private String name;
    @Positive @Max(999999)
    private int price;
    @NotEmpty
    private List<Item> items;
    @Getter
    public static class Item{
        @Positive
        private Long itemId;
        @Positive @Max(10)
        private int quantity;
    }
}