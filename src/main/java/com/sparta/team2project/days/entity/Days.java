package com.sparta.team2project.days.entity;

import com.sparta.team2project.posts.dto.DayRequestDto;
import com.sparta.team2project.posts.dto.DayResponseDto;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.schedules.entity.Schedules;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "days")
@Getter
@NoArgsConstructor
public class Days {
    // 여행날짜 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate chosenDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id",nullable = false)
    private Posts posts;

    @OneToMany(mappedBy = "days", cascade = {CascadeType.REMOVE})
    private List<Schedules> schedulesList = new ArrayList<>();

    public Days(DayRequestDto dayRequestDto, Posts posts) {
        this.chosenDate = dayRequestDto.getChosenDate();
        this.posts = posts;
        this.schedulesList = dayRequestDto.getSchedulesList();

    }

    public void updateChosenDate(DayRequestDto dayRequestDto) {
        this.chosenDate = dayRequestDto.getChosenDate();
    }

    public void updateDays(DayRequestDto dayRequestDto){
        this.chosenDate = dayRequestDto.getChosenDate();
        this.schedulesList = dayRequestDto.getSchedulesList();
    }
}
