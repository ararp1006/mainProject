package com.mainproject.be28.image.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.awt.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class ImageInfo {
    @Column(nullable = false)
    private String imageName;
    @Column(nullable = false)
    private String oriName;
    @Column(nullable = false)
    private String filePath="C:/Users/박아름/Desktop/mainProject-main/src/main/resources/static/images";
    @Transient
    private final String baseUrl = "http://main-test-aream.s3-website.ap-northeast-2.amazonaws.com";


}