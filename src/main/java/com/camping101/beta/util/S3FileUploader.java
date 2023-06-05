package com.camping101.beta.util;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.UUID;

import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3FileUploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public String uploadFileAndGetURL(MultipartFile multipartFile) {

        if (multipartFile.isEmpty() || StringUtils.isNullOrEmpty(multipartFile.getOriginalFilename())) {
            return "";
        }

        try {

            String originalFileName = multipartFile.getOriginalFilename();
            String extension = getExtension(originalFileName);
            String contentType = getContentType(extension);
            String changedFilename =
                UUID.randomUUID().toString().replace("-", "").replace("_","") + "." + extension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            amazonS3.putObject(
                new PutObjectRequest(bucket, changedFilename, multipartFile.getInputStream(),
                    metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3.getUrl(bucket, changedFilename).toString();

        } catch (IOException | SdkClientException e) {

            log.info("이미지 저장 실패 : 연결 이상");

            e.printStackTrace();

        } catch (Exception e) {

            log.info("이미지 저장 실패 : (이미지가 없을 가능성이 높습니다.)" + e.getMessage());

            e.printStackTrace();
        }

        return "";
    }

    private String getExtension(String fileName) {
        String[] fileNameParts = fileName.split("\\.");
        String extension = fileNameParts[fileNameParts.length - 1];
        return extension;
    }

    private String getContentType(String extension) {

        String contentType = "";

        switch (extension) {
            case "jpg":
                contentType = "image/jpg";
                break;
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "txt":
                contentType = "text/txt";
                break;
            case "csv":
                contentType = "text/csv";
                break;
        }

        return contentType;
    }

}
