package com.mainproject.be28.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;


@Getter
public class MultiResponseDto<T> extends BaseResponse{
    private List<T> data;
    private PageInfo pageInfo;


    public MultiResponseDto(List<T> data, Page page, HttpStatus status) {
        this.status = status.value();
        this.message = status.getReasonPhrase();
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber() + 1,
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}