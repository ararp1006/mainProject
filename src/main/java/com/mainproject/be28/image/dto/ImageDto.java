package com.mainproject.be28.image.dto;

import com.mainproject.be28.image.entity.ImageInfo;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ImageDto {
    private boolean isFileInserted;
    private String uploadStatus;
    private ImageInfo content;
}
