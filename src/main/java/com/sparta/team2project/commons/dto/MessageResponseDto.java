package com.sparta.team2project.commons.dto;

import lombok.Getter;

@Getter
public class MessageResponseDto {
    private String msg;
    private int statusCode;

    public MessageResponseDto(String msg, Integer statusCode){
        this.msg = msg;
        this.statusCode = statusCode;
    }
}


