package com.sparta.team2project.pictures.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.pictures.dto.PicturesResponseDto;
import com.sparta.team2project.pictures.dto.UploadResponseDto;
import com.sparta.team2project.pictures.entity.Pictures;
import com.sparta.team2project.pictures.repository.PicturesRepository;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.s3.AmazonS3ResourceStorage;
import com.sparta.team2project.s3.FileDetail;
import com.sparta.team2project.schedules.entity.Schedules;
import com.sparta.team2project.schedules.repository.SchedulesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PicturesService {
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;
    private final AmazonS3Client amazonS3Client;
    private final PicturesRepository picturesRepository;
    private final SchedulesRepository schedulesRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public FileDetail save(MultipartFile multipartFile) {
        FileDetail fileDetail = FileDetail.multipartOf(multipartFile);
        amazonS3ResourceStorage.store(fileDetail.getPath(), multipartFile);
        return fileDetail;
    }

    public UploadResponseDto uploadPictures(Long schedulesId, List<MultipartFile> files) {
        // 파일 등록
        try {
            List<Pictures> picturesList = new ArrayList<>();
            List<PicturesResponseDto> picturesResponseDtoList = new ArrayList<>(3);
            for(MultipartFile file: files){
                // 1. 파일 정보를 picturesResponseDtoList에 저장
                String picturesName = file.getOriginalFilename();
                String picturesURL = "https://" + bucket + "/" + picturesName;
                PicturesResponseDto picturesResponseDto = new PicturesResponseDto(picturesURL, picturesName);
                picturesResponseDtoList.add(picturesResponseDto);
                // 2. Repository에 파일 정보를 저장하기 위해 PicturesList에 저장(schedulesId 필요)
                Schedules schedules = schedulesRepository.findById(schedulesId).orElseThrow(
                        () -> new CustomException(ErrorCode.ID_NOT_MATCH)
                );
                Pictures pictures = new Pictures(schedules, picturesURL, picturesName);
                picturesList.add(pictures);
                // 3. 사진을 메타데이터 및 정보와 함께 S3에 저장
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                amazonS3Client.putObject(bucket, picturesName, file.getInputStream(), metadata);
            }
            // 4. Repository에 Pictures리스트를 저장
            picturesRepository.saveAll(picturesList);
            // 5. 성공 메시지 DTO와 함께 picturesResponseDtoList를 반환
            MessageResponseDto messageResponseDto = new MessageResponseDto("아래 파일들이 등록되었습니다.", 200);
            UploadResponseDto uploadResponseDto = new UploadResponseDto(picturesResponseDtoList, messageResponseDto);
            return uploadResponseDto;
        }
        // 실패시 예외 처리
        catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.S3_NOT_UPLOAD);
        }

    }
}
