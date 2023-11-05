package com.mainproject.be28.image.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

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
    private String filePath;
    @Transient
    private final String baseUrl = "http://main-test-aream.s3-website.ap-northeast-2.amazonaws.com/";
}