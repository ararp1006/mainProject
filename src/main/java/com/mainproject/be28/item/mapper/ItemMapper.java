package com.mainproject.be28.item.mapper;

import com.mainproject.be28.image.entity.ImageInfo;
import com.mainproject.be28.item.dto.ItemDto;
import com.mainproject.be28.item.dto.OnlyItemResponseDto;
import com.mainproject.be28.item.entity.Item;
import com.mainproject.be28.review.dto.ReviewResponseDto;
import com.mainproject.be28.review.entity.Review;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    default Item itemDtoToItem(ItemDto.Post itemDto) {
        return Item.builder().name(itemDto.getName())
                .status(itemDto.getStatus())
                .detail(itemDto.getDetail())
                .price(Math.toIntExact(itemDto.getPrice()))
                .color(itemDto.getColor())
                .brand(itemDto.getBrand())
                . category(itemDto.getCategory())
                .build();
    }
    default List<OnlyItemResponseDto> itemToItemResponseDto(List<Item> items){
        return items.stream().map(item ->{
            OnlyItemResponseDto onlyItemResponseDto = OnlyItemResponseDto.builder()
                    .itemId(item.getItemId())
                    .name(item.getName())
                    .price((long) item.getPrice())
                    .detail(item.getDetail())
                    .status(item.getStatus())
                    .build();

            if(item.getItemImage()!=null){
                ImageInfo imageInfo = item.getItemImage().get(1).getImageInfo();
                onlyItemResponseDto.setImagePath(imageInfo.getBaseUrl()+imageInfo.getFilePath()+imageInfo.getImageName());
            }

            return onlyItemResponseDto;
        }).collect(Collectors.toList());
    }


    default List<ReviewResponseDto> getReviewsResponseDto(Item item) {
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        List<Review> reviewList =  item.getReviews();
        if(reviewList != null) {
            for (Review review : reviewList) {
                ReviewResponseDto reviewResponseDto =
                        ReviewResponseDto.builder()
                                .itemName(review.getItem().getName())
                                .memberName(review.getMember().getName())
                                .content(review.getContent())
                                .score(review.getScore()) //상품별점추가
                                .modifiedAt(review.getModifiedAt())//수정일 추가
                                .createdAt(review.getCreatedAt())
                                .likeCount(review.getLikeCount())
                                .unlikeCount(review.getUnlikeCount())
                                .build();
                reviewResponseDtos.add(reviewResponseDto);
            }
        }
        return reviewResponseDtos;
    }
}