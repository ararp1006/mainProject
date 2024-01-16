package com.mainproject.be28.item.service;

import com.mainproject.be28.exception.BusinessLogicException;
import com.mainproject.be28.image.entity.ItemImage;
import com.mainproject.be28.image.service.ImageService;
import com.mainproject.be28.image.repository.ItemImageRepository;
import com.mainproject.be28.item.dto.ItemDto;
import com.mainproject.be28.item.dto.OnlyItemResponseDto;
import com.mainproject.be28.item.entity.Item;
import com.mainproject.be28.item.exception.ItemException;
import com.mainproject.be28.item.mapper.ItemMapper;
import com.mainproject.be28.item.repository.ItemRepository;
import com.mainproject.be28.item.dto.ItemSearchConditionDto;
import com.mainproject.be28.review.entity.Review;
import com.mainproject.be28.utils.CustomBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper mapper;
    private final ImageService imageService;
    private final ItemImageRepository itemImageRepository;
    private final CustomBeanUtils<Item> beanUtils;

    public ItemService(ItemRepository itemRepository, ItemMapper mapper, ImageService imageService, ItemImageRepository itemImageRepository, CustomBeanUtils<Item> beanUtils) {
        this.itemRepository = itemRepository;
        this.mapper = mapper;
        this.imageService = imageService;
        this.itemImageRepository = itemImageRepository;
        this.beanUtils = beanUtils;
    }

    /* *******************퍼블릭 메서드******************* */

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item updateItem(Long itemId, Item itemPatcher){
        Item item = findItem(itemId);
        item.patchItem(itemPatcher);
        return itemRepository.save(item);
    }
    public void uploadImages(Long itemId, List<MultipartFile> files) {
        Item item = findItem(itemId);
        if (item != null) {
            List<ItemImage> uploadedImages = new ArrayList<>();
            for (MultipartFile file : files) {
                ItemImage image = imageService.uploadItemImage(file, item);
                if (image != null) {
                    uploadedImages.add(image);
                }
            }
            if (!uploadedImages.isEmpty()) {
                item.setItemImage(uploadedImages);
                itemRepository.save(item);
            }
        }
    }

    public Item findItem(long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Item item = optionalItem.orElseThrow(() -> new BusinessLogicException(ItemException.ITEM_NOT_FOUND));

        // Reviews가 null이면 빈 리스트로 초기화하여 NullPointerException 방지
        List<Review> reviews = item.getReviews() != null ? item.getReviews() : Collections.emptyList();

        item.setReviewCount(reviews.size());
        item.setScore(updateScore(item));

        return item;
    }



    public void deleteItem(long itemId) {
        Item findItem = findItem(itemId);
        itemRepository.delete(findItem);
    }

    private Double updateScore(Item item) {
        if (item == null || item.getReviews() == null) {
            return 0.0; // 또는 다른 기본값 반환
        }

        if (item.getScore() == null) {
            item.setScore(0.0);
        }

        List<Review> reviews = item.getReviews();

        if (reviews.isEmpty()) {
            return item.getScore(); // 리뷰가 없으면 현재 스코어를 반환
        }

        double score = 0D;
        for (Review review : reviews) {
            score += review.getScore();
        }

        score /= reviews.size();
        score = (double) Math.round(score * 100) / 100;
        return score;
    }


    public Page<Item> searchItems(String search, int page, int size, String sort,
                                        Sort.Direction direction){
        if(search.trim().equals("")){
            return new PageImpl<Item>(new ArrayList<>(), PageRequest.ofSize(size),0);
        }

        PageRequest pageRequest = makePageRequest(page, size, sort, direction);
        return itemRepository.findAllByForSaleIsTrueAndNameContains(search, pageRequest);
    }
    private PageRequest makePageRequest(int page, int size, String sort, Sort.Direction direction) {
        PageRequest pageRequest = null;
        if(direction.isAscending()){
            pageRequest =
                    PageRequest.of(page-1, size, Sort.by(sort).ascending());
        } else if(direction.isDescending()){
            pageRequest =
                    PageRequest.of(page-1, size, Sort.by(sort).descending());
        }
        return pageRequest;
    }
}


