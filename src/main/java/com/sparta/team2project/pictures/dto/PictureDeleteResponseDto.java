package com.sparta.team2project.pictures.dto;

import com.sparta.team2project.commons.dto.MessageResponseDto;

import java.util.List;

public class PictureDeleteResponseDto {
    private final MessageResponseDto messageResponseDto;
    private final PicturesResponseDto picturesResponseDto;

    public PictureDeleteResponseDto(PicturesResponseDto picturesResponseDto, MessageResponseDto messageResponseDto) {
        this.messageResponseDto = messageResponseDto;
        this.picturesResponseDto = picturesResponseDto;
    }
}
