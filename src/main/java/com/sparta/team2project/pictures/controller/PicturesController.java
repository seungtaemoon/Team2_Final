package com.sparta.team2project.pictures.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.pictures.dto.PicturesResponseDto;
import com.sparta.team2project.pictures.dto.UploadResponseDto;
import com.sparta.team2project.pictures.service.PicturesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PicturesController {

    private final PicturesService picturesService;

    @PostMapping("/schedules/{schedulesId}/pictures")
    public UploadResponseDto uploadPictures(@PathVariable("schedulesId") Long schedulesId,
                                            @RequestPart("file") List<MultipartFile> files){
        if(files.size() <= 3){
            return picturesService.uploadPictures(schedulesId, files);
        }
        else{
            throw new CustomException(ErrorCode.EXCEED_PICTURES_LIMIT);
        }
    }

}