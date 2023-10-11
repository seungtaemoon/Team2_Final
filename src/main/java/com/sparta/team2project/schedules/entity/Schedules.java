package com.sparta.team2project.schedules.entity;

import com.sparta.team2project.tripdate.entity.TripDate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;


@Entity
@Getter
@NoArgsConstructor
@Table(name="schedules")
public class Schedules {
    // 일정 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // 세부일정 1차 카테고리
    @Column(name = "scheduleCategory", nullable = false)
    private SchedulesCategory schedulesCategory;
    // 비용
    @Column(name = "costs", nullable = false)
    private int costs;
    // 관광지 이름
    @Column(name = "placeName", nullable = false)
    private String placeName;
    // 내용
    @Column(name = "contents", nullable = false)
    private String contents;
    // 소요시간
    @Column(name = "timeSpent", nullable = false)
    private String timeSpent;
    // URL
    @Column(name = "referenceURL", nullable = true)
    private String referenceURL;

    // 날짜별 여행계획(Days)와 양방향 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tripDate_id")
    private TripDate tripDate;

    public Schedules(TripDate tripDate, Schedules schedules) {
        this.tripDate = tripDate;
        this.schedulesCategory = schedules.getSchedulesCategory();
        this.contents = schedules.getContents();
        this.costs = schedules.getCosts();
        this.placeName =schedules.getPlaceName();
        this.timeSpent=schedules.getTimeSpent();
        this.referenceURL=schedules.getReferenceURL();
    }
}
