package com.mainproject.be28.item.repository;

import com.mainproject.be28.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository  extends  JpaRepository<Item, Long>, CustomItemRepository {
    Page<Item> findAllByForSaleIsTrueAndNameContains(String search, Pageable pageable);


}
