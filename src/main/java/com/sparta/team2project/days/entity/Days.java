package com.sparta.team2project.days.entity;

import com.sparta.team2project.posts.dto.DayRequestDto;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate chosenDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id",nullable = false)
    private Posts posts;

    @OneToMany(mappedBy = "days", cascade = {CascadeType.REMOVE})
    private List<Schedules> scheduleList = new ArrayList<>();

    public Days(DayRequestDto dayRequestDto, Posts posts) {
        this.chosenDate = dayRequestDto.getChosenDate();
        this.posts = posts;
        this.scheduleList = dayRequestDto.getScheduleList();

    }
}
