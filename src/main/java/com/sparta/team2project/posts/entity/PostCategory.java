package com.sparta.team2project.posts.entity;

import lombok.Getter;

@Getter
public enum PostCategory {

    FRIENDSHIP("우정"),
    FAMILY("가족"),
    COUPLE("커플"),
    ALONE("나홀로"),
    EATING("식도락");
    private final String theme;

    PostCategory(String theme){
        this.theme=theme;
    }
    public String getTheme() {
        return theme;
    }

}
