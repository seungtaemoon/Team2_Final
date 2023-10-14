package com.sparta.team2project.pictures.entity;

import com.sparta.team2project.pictures.dto.PicturesRequestDto;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.schedules.dto.SchedulesRequestDto;
import com.sparta.team2project.schedules.entity.Schedules;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pictures")
@Getter
@NoArgsConstructor
public class Pictures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String pictureURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedules_id",nullable = false)
    private Schedules schedules;

    public Pictures(String pictureURL){
        this.pictureURL = pictureURL;
    }

    public void updatePictures(String pictureURL){
        this.pictureURL = pictureURL;
    }
}
