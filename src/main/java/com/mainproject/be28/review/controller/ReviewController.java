package com.mainproject.be28.review.controller;

import com.mainproject.be28.item.service.ItemService;
import com.mainproject.be28.member.entity.Member;
import com.mainproject.be28.member.mapper.MemberMapper;
import com.mainproject.be28.member.service.MemberService;
import com.mainproject.be28.review.dto.ReviewPatchDto;
import com.mainproject.be28.review.dto.ReviewPostDto;
import com.mainproject.be28.review.dto.ReviewResponseDto;
import com.mainproject.be28.review.entity.Review;
import com.mainproject.be28.review.mapper.ReviewMapper;
import com.mainproject.be28.review.service.ReviewService;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/item/{item-id}/review")
@Validated
@Slf4j
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewMapper mapper;
    private final ItemService itemService;
    private final MemberMapper memberMapper;
    private final MemberService memberService;


    @PostMapping("/new/{score}")// 리뷰등록
    public ResponseEntity createReview(Principal principal,@PathVariable("item-id")long itemId,
                                       @RequestBody @Valid  ReviewPostDto reviewPostDto,
                                       @PathVariable("score") int score) {
        log.info("######CREATEREVIEW######");
        memberService.checkMemberExist(principal.getName());
        reviewPostDto.setScore(score);
        reviewPostDto.setItemId(itemId);
        reviewService.createReview(mapper.ReviewPostDtoToReview(reviewPostDto));
        return new ResponseEntity<>( HttpStatus.CREATED);

    }
    @PatchMapping("/{review-id}")// 리뷰수정
    public ResponseEntity patchReview(Principal principal,@PathVariable("review-id") @Positive long reviewId,
                                      @Valid @RequestBody ReviewPatchDto reviewPatchDto){
        log.info("######PATCH REVIEW######");
        memberService.checkMemberExist(principal.getName());
        reviewPatchDto.setReviewId(reviewId);
        Review review = mapper.reviewPatchDtoToReview(reviewPatchDto);
        Review response = reviewService.updateReview(review);
        return  new ResponseEntity<>(mapper.reviewToReviewResponseDto(response),HttpStatus.OK);
    }
    @DeleteMapping("/{review-id}") //리뷰삭제
    public ResponseEntity deleteReview(Principal principal,@PathVariable("review-id") @Positive long reviewId){
        log.info("######DELETE REVIEW######");
        memberService.checkMemberExist(principal.getName());
        reviewService.deleteReview(reviewId);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/{reviewid}/like")//리뷰 좋아요
    public ResponseEntity addLike(Principal principal,@PathVariable("reviewid") Long reviewId) {
        log.info("######LIKE REVIEW######");
        memberService.checkMemberExist(principal.getName());
        Review likeReview = reviewService.addLike(reviewId);
        return new ResponseEntity<>( mapper.reviewToReviewResponseDto( likeReview), HttpStatus.OK);
        }

    @PostMapping("/{reviewid}/unlike")
    public ResponseEntity addDislike(Principal principal,@PathVariable("reviewid") Long reviewId) {
            log.info("######DISLIKE REVIEW######");
            memberService.checkMemberExist(principal.getName());
            Review unlikeReview = reviewService.addDislike(reviewId);
            return new ResponseEntity<>(mapper.reviewToReviewResponseDto( unlikeReview), HttpStatus.OK);
        }
    //리뷰 사진 올리기
   @PostMapping("/{reviewId}/image")
    public ResponseEntity postItemImage(Principal principal,@ApiParam(value = "File to upload", required = true) @PathVariable("reviewId") @Positive Long reviewId,
                                        @RequestPart("file") List<MultipartFile> files
    ) {
       log.info("######UPLOAD IMAGE REVIEW ######");
        reviewService.uploadImages(reviewId, files);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}


