package com.mainproject.be28.image.repository;

import com.mainproject.be28.image.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemImageRepository  extends JpaRepository<ItemImage, Long> {

}
