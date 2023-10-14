package com.sparta.team2project.pictures.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.pictures.dto.PicturesRequestDto;
import com.sparta.team2project.pictures.dto.PicturesResponseDto;
import com.sparta.team2project.pictures.entity.Pictures;
import com.sparta.team2project.s3.AmazonS3ResourceStorage;
import com.sparta.team2project.s3.FileDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PicturesService {
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public FileDetail save(MultipartFile multipartFile) {
        FileDetail fileDetail = FileDetail.multipartOf(multipartFile);
        amazonS3ResourceStorage.store(fileDetail.getPath(), multipartFile);
        return fileDetail;
    }

    public PicturesResponseDto uploadPictures(MultipartFile file) {
        // 파일 등록
        try {
            String picturesName = file.getOriginalFilename();
            String picturesURL = "https://" + bucket + "/" + picturesName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, picturesName, file.getInputStream(), metadata);
            MessageResponseDto messageResponseDto = new MessageResponseDto("파일이 등록되었습니다.", 200);
            PicturesResponseDto picturesResponseDto = new PicturesResponseDto(picturesURL, messageResponseDto);
            return picturesResponseDto;
        }
        // 실패시 예외 처리
        catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.S3_NOT_UPLOAD);
        }

    }
}
