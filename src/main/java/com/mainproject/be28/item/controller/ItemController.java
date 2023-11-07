package com.mainproject.be28.item.controller;

import com.mainproject.be28.exception.BusinessLogicException;
import com.mainproject.be28.exception.ExceptionCode;
import com.mainproject.be28.item.dto.ItemDto;
import com.mainproject.be28.item.dto.OnlyItemResponseDto;
import com.mainproject.be28.item.entity.Item;
import com.mainproject.be28.item.exception.ItemException;
import com.mainproject.be28.item.mapper.ItemMapper;
import com.mainproject.be28.item.dto.ItemSearchConditionDto;
import com.mainproject.be28.item.service.ItemService;
import com.mainproject.be28.response.MultiResponseDto;
import com.mainproject.be28.response.SingleResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
@Validated
@Slf4j

public class ItemController {
    private final static String ITEM_DEFAULT_URL = "/item";
    private final ItemService itemService;
    private final ItemMapper mapper;
    private final int itemListSize = 8;

    //관리자가 개별상품 생성하기
    @PostMapping("/admin/items")
    public ResponseEntity createAdminItem(@RequestBody @Valid ItemDto.Post itemDto){
        log.info("--------createItem-------");
        Item item = mapper.itemDtoToItem(itemDto);
        Item savedItem =itemService.createItem(item);
        return new ResponseEntity(savedItem.getItemId(), HttpStatus.CREATED);
    }

    //관리자가 개별상품 수정하기
    @PatchMapping("/admin/items/{itemId}")
    public ResponseEntity updateAdminItem(@PathVariable("itemId") @Positive Long itemId,
                                             @RequestBody @Valid ItemDto.Post itemDto){
        log.info("--------updateITEM-------");
        Item itemPatcher = mapper.itemDtoToItem(itemDto);
        itemService.updateItem(itemId, itemPatcher);
        return new ResponseEntity(HttpStatus.OK);
    }

    //관리자가 개별상품 삭제하기
    @DeleteMapping("admin/items/{item-id}")
    public ResponseEntity deleteAdminItem(@PathVariable("item-id") @Positive long itemId){

        itemService.deleteItem(itemId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/admin/items/{itemId}/image")
    public ResponseEntity postItemImage(@ApiParam(value = "File to upload", required = true) @PathVariable("itemId") @Positive Long itemId,
                                           @RequestPart("file") List<MultipartFile> files
    ) {
        log.info("------uploadItemsImage------");
        itemService.uploadImages(itemId, files);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity getItem(@PathVariable("itemId") @Positive long itemId){

        Item item = itemService.findItem(itemId);
        if(item == null) {
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
        }

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        List<OnlyItemResponseDto> itemResponse = mapper.itemToItemResponseDto(itemList);

        return new ResponseEntity<>(itemResponse.get(0), HttpStatus.OK);
    }

    @GetMapping("/items/search")
    public ResponseEntity getSearcheItemList(@RequestParam @Positive int page,
                                                 @RequestParam String name) {
        log.info("------getsearchItem------");
        Page<Item> itemPage =
                itemService.searchItems(name, page, itemListSize, "id", Sort.Direction.ASC);

        List<Item> items = itemPage.getContent();
        List<OnlyItemResponseDto> response = mapper.itemToItemResponseDto(items);

        return new ResponseEntity(new MultiResponseDto(response,itemPage,HttpStatus.CREATED), HttpStatus.OK);
    }


}
