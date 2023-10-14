package com.sparta.team2project.pictures.dto;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.pictures.entity.Pictures;
import lombok.Getter;

@Getter
public class PicturesResponseDto {
    private final String picturesURL;
    private final String picturesName;

    public PicturesResponseDto(String picturesURL, String picturesName) {
        this.picturesURL = picturesURL;
        this.picturesName = picturesName;
    }
}
