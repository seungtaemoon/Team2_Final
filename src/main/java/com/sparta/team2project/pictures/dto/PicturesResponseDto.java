package com.sparta.team2project.pictures.dto;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.pictures.entity.Pictures;
import lombok.Getter;

@Getter
public class PicturesResponseDto {
    private final String picturesURL;
    private final MessageResponseDto messageResponseDto;

    public PicturesResponseDto(String picturesURL, MessageResponseDto messageResponseDto) {
        this.picturesURL = picturesURL;
        this.messageResponseDto = messageResponseDto;
    }
}
