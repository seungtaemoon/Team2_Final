package com.sparta.team2project.schedules.entity;

import lombok.Getter;

@Getter
public enum SchedulesCategory {
    TRANSPORTATION("교통"),
    ACCOMMODATION("숙박"),
    CONTENTS("즐길거리"),
    FOOD("음식");
    private final String category;

    SchedulesCategory(String category){
        this.category = category;
    }
}
