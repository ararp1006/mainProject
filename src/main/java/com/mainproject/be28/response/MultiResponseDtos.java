package com.mainproject.be28.response;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class MultiResponseDtos<T> extends BaseResponse {
    private List<T> data;
    private PageInfo pageInfo;

    public MultiResponseDtos(List<T> data, Page page) {
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber() + 1,
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}