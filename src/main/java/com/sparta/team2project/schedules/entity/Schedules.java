package com.sparta.team2project.schedules.entity;

import com.sparta.team2project.days.entity.Days;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    // 세부일정 2차 카테고리
    @Column(name = "details", nullable = false)
    private String details;
    // 비용
    @Column(name = "costs", nullable = false)
    private int costs;
    // 관광지 이름
    @Column(name = "placeName", nullable = false)
    private String placeName;
    // 내용
    @Column(name = "contents", nullable = false)
    private String contents;
    // 시작날짜
    @Column(name = "startTime", nullable = false)
    private LocalTime startTime;
    // 종료날짜
    @Column(name = "endTime", nullable = false)
    private LocalTime endTime;

    // 날짜별 여행계획(Days)와 양방향 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="days_id")
    private Days days;

    public Schedules(Days days,Schedules schedules) {
        this.days=days;
        this.schedulesCategory = schedules.getSchedulesCategory();
        this.contents = schedules.getContents();
        this.details = schedules.getDetails();
        this.costs = schedules.getCosts();
        this.placeName =schedules.getPlaceName();
        this.startTime=schedules.getStartTime();
        this.endTime=schedules.getEndTime();

    }
}
