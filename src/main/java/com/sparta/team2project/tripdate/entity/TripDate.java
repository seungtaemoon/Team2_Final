package com.sparta.team2project.tripdate.entity;

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
@Table(name = "tripDate")
@Getter
@NoArgsConstructor
public class TripDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate chosenDate;

    @Column(nullable = false)
    private String subTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id",nullable = false)
    private Posts posts;

    @OneToMany(mappedBy = "tripDate", cascade = {CascadeType.REMOVE})
    private List<Schedules> schedulesList = new ArrayList<>();

    public TripDate(DayRequestDto dayRequestDto, Posts posts) {
        this.chosenDate = dayRequestDto.getChosenDate();
        this.subTitle = dayRequestDto.getSubTitle();
        this.posts = posts;
        this.schedulesList = dayRequestDto.getSchedulesList();

    }
}
