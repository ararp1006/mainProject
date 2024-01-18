package com.mainproject.be28.item.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mainproject.be28.config.S3Config;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
@Validated
@Slf4j

public class ItemController {
    private final static String ITEM_DEFAULT_URL = "/item";
    private final ItemService itemService;
    private final ItemMapper mapper;
    private final int itemListSize = 8;
    private final S3Config s3Config;


    // 관리자가 개별 상품 생성하기
    @PostMapping("/admin/items")
    public String createOrUpdateAdminItem(
            @PathVariable(value = "itemId", required = false) @Positive Long itemId,
            @RequestPart(value = "file", required = false) List<MultipartFile> files,
            @RequestPart(value = "itemDto") @Valid ItemDto.Post itemDto
    ) {
        log.info("--------createItem-------");
        Item item = mapper.itemDtoToItem(itemDto);
        Item savedItem = itemService.createItem(item);

        log.info("------uploadItemsImage------");
        itemService.uploadImages(savedItem.getItemId(), files);

        log.info("------Item Upload------");
        return "itemUpload";
    }

    @GetMapping("/itemsPage")
    public String  itemsPage(Model model) {
        List<Item> itemList = itemService.getAllItem();
        List<String> itemNames = itemList.stream().map(Item::getName).collect(Collectors.toList());
        ObjectMapper objectMapper = new ObjectMapper();
        String itemsJson;
        try {
            itemsJson = objectMapper.writeValueAsString(itemNames);
        } catch (JsonProcessingException e) {
            // JSON 변환 중 오류가 발생할 경우 예외 처리
            itemsJson = "[]"; // 빈 배열로 초기화하거나 다른 예외 처리 방법을 선택할 수 있습니다.
        }
        model.addAttribute("items", itemsJson);
        model.addAttribute("accessKey",s3Config.getAccessKey());
        model.addAttribute("secretKey",s3Config.getSecretKey());

        return "itemView";
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

    //아이템 상세조회
    @GetMapping("/{itemId}")
    public ModelAndView getItem(@PathVariable("itemId") @Positive long itemId) {
        Item item = itemService.findItem(itemId);

        if (item == null) {

            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("errorMessage", "Item not found");
            return modelAndView;
        }

        List<Item> itemList = Collections.singletonList(item);
        List<OnlyItemResponseDto> itemResponse = mapper.itemToItemResponseDto(itemList);

        ModelAndView modelAndView = new ModelAndView("itemView"); // JSP 페이지의 이름
        modelAndView.addObject("item", itemResponse.get(0));

        return modelAndView;
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
