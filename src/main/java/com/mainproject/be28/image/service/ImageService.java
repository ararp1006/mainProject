package com.mainproject.be28.image.service;

import com.mainproject.be28.exception.BusinessLogicException;
import com.mainproject.be28.image.entity.ImageInfo;
import com.mainproject.be28.image.entity.ItemImage;
import com.mainproject.be28.image.entity.MemberImage;
import com.mainproject.be28.image.entity.ReviewImage;
import com.mainproject.be28.image.exception.ImageUploadException;
import com.mainproject.be28.image.repository.ItemImageRepository;
import com.mainproject.be28.image.util.ImageSort;
import com.mainproject.be28.item.entity.Item;
import com.mainproject.be28.member.entity.Member;
import com.mainproject.be28.review.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private  final FileManager fileManager;
    private final UploadImageS3 uploadImageS3;
    private final ItemImageRepository itemImageRepository;
    public ItemImage uploadItemImage(MultipartFile mf, Item item){
        long time = System.currentTimeMillis();
        String originalFilename = mf.getOriginalFilename();
        String saveFileName = String.format("%d_%s", time, originalFilename.replaceAll(" ", ""));
        String filePath = ImageSort.Item_IMAGE.getPath();

        String savedPath = createAndUploadFile(mf,saveFileName, filePath);
        log.info("Saved Path : "+savedPath);

        ItemImage itemImage = ItemImage.builder()
                .item(item)
                .imageInfo(new ImageInfo(saveFileName, originalFilename, filePath))
                .build();

        saveImageToDatabase(itemImage);

        return itemImage;
    }

    public MemberImage uploadMemberImage (MultipartFile mf, Member member){
        long time = System.currentTimeMillis();
        String originalFilename = mf.getOriginalFilename();
        String saveFileName = String.format("%d_%s", time, originalFilename.replaceAll(" ", ""));
        String filePath = ImageSort.Member_IMAGE.getPath();

        String savedPath = createAndUploadFile(mf,saveFileName, filePath);
        log.info("Saved Path : "+savedPath);

        return MemberImage.builder().member(member)
                .imageInfo(new ImageInfo(saveFileName, originalFilename, filePath))
                .build();
    }
    public ReviewImage uploadReviewImage(MultipartFile mf, Review review){
        long time = System.currentTimeMillis();
        String originalFilename = mf.getOriginalFilename();
        String saveFileName = String.format("%d_%s", time, originalFilename.replaceAll(" ", ""));
        String filePath = ImageSort.Review_IMAGE.getPath();

        String savedPath = createAndUploadFile(mf,saveFileName, filePath);
        log.info("Saved Path : "+savedPath);

        return  ReviewImage.builder().review(review)
                .imageInfo(new ImageInfo(saveFileName, originalFilename, filePath))
                .build();
    }

    // 임시 파일 생성 & 업데이트 & 임시 파일 삭제
    private  String createAndUploadFile(MultipartFile mf, String saveFileName, String filePath) {
        // 파일 생성
        File uploadFile = null;
        try {
            Optional<File> uploadFileOpt = fileManager.convertMultipartFileToFile(mf);
            if (uploadFileOpt.isEmpty()) {
                throw new BusinessLogicException(ImageUploadException.IMAGE_NOT_CONVERTED);
            }
            uploadFile = uploadFileOpt.get();

            // 파일 업로드
            String saveFilePath = uploadImageS3.upload(uploadFile, filePath, saveFileName);

            return File.separator + saveFilePath;

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessLogicException(ImageUploadException.IMAGE_NOT_UPLOADED);
        } finally {
            // 파일 삭제
            if (uploadFile != null) {
                uploadFile.delete();
            }
        }
    }
    private void saveImageToDatabase(ItemImage itemImage) {
        itemImageRepository.save(itemImage);
    }
}