package com.mainproject.be28.image.repository;

import com.mainproject.be28.image.entity.ItemImage;
import com.mainproject.be28.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemImageRepository  extends JpaRepository<ItemImage, Long> {

    }




