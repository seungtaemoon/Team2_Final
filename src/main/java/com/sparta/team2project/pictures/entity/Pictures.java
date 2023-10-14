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
    private String picturesURL;

    @Column(nullable = true)
    private String picturesName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedules_id",nullable = false)
    private Schedules schedules;

    public Pictures(Schedules schedules, String picturesURL, String picturesName){
        this.schedules = schedules;
        this.picturesURL = picturesURL;
        this.picturesName = picturesName;
    }

    public void updatePictures(String picturesURL, String picturesName){
        this.picturesURL = picturesURL;
        this.picturesName = picturesName;
    }
}
