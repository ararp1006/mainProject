package com.mainproject.be28.review.mapper;

import com.mainproject.be28.complain.dto.ComplainPatchDto;
import com.mainproject.be28.complain.entity.Complain;
import com.mainproject.be28.review.dto.ReviewPatchDto;
import com.mainproject.be28.review.dto.ReviewPostDto;
import com.mainproject.be28.review.dto.ReviewResponseDto;
import com.mainproject.be28.review.entity.Review;
import lombok.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "itemId", target = "item.itemId") //itemId 필드 값을 가져와서, Complain 객체의 item 필드의 itemId로 매핑
    @Mapping(source = "memberId", target = "member.memberId")
    Review ReviewPostDtoToReview(ReviewPostDto reviewPostDTO);

    Review reviewPatchDtoToReview(ReviewPatchDto reviewPatchDto);
    @Mapping(source = "item.name", target ="itemName")
    @Mapping(source = "member.name", target = "memberName")
    ReviewResponseDto reviewToReviewResponseDto(Review review);

}