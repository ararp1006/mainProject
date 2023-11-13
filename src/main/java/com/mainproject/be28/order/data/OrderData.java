package com.mainproject.be28.order.data;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
@Getter
@Setter
public class OrderData {
    @NotNull(message = "PG사 코드는 필수입니다.")
    private String pg;

    @NotNull(message = "결제 방법은 필수입니다.")
    private String payMethod;

    @NotNull(message = "주문자이름은 필수입니다.")
    private String orderName;

    @Positive(message = "결제 금액은 0보다 커야 합니다.")
    private long totalPrice;

    @Pattern(regexp = "^\\d{10,11}$", message = "올바른 전화번호를 입력해주세요.")
    private String orderPhone;

    @NotNull(message = "구매자 주소는 필수입니다.")
    private String Addressee;

    @Pattern(regexp = "^\\d{5}$", message = "올바른 우편번호를 입력해주세요.")
    private String zipCode;
}