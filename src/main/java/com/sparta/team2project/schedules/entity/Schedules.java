package com.sparta.team2project.schedules.entity;

import com.sparta.team2project.commons.timestamped.TimeStamped;
import com.sparta.team2project.tripdate.entity.TripDate;
import com.sparta.team2project.schedules.dto.SchedulesRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;


@Entity
@Getter
@NoArgsConstructor
@Table(name="schedules")
public class Schedules extends TimeStamped {
    // 일정 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // 세부일정 1차 카테고리
    @Column(name = "scheduleCategory", nullable = false)
    private SchedulesCategory schedulesCategory;
    // 세부일정 2차 카테고리
//    @Column(name = "details", nullable = false)
//    private String details;
    // 비용
    @Column(name = "costs", nullable = false)
    private int costs;
    // 관광지 이름
    @Column(name = "placeName", nullable = false)
    private String placeName;
    // 내용
    @Column(name = "contents", nullable = false)
    private String contents;
//    // 시작날짜
//    @Column(name = "startTime", nullable = false)
//    private LocalTime startTime;
//    // 종료날짜
//    @Column(name = "endTime", nullable = false)
//    private LocalTime endTime;
    // 소요 시간
    @Column(name = "timeSpent", nullable = false)
    private String timeSpent;
    // 참조 자료 URL
    @Column(name = "referenceURL", nullable = true)
    private String referenceURL;

    // 날짜별 여행계획(TripDate)와 양방향 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tripDate_id")
    private TripDate tripDate;

    public Schedules(TripDate tripDate, Schedules schedules) {
        this.tripDate = tripDate;
        this.schedulesCategory=schedules.getSchedulesCategory();
//        this.endTime=schedules.getEndTime();
//        this.startTime=schedules.getStartTime();
        this.timeSpent = schedules.getTimeSpent();
        this.costs=schedules.getCosts();
        this.contents=schedules.getContents();
        this.placeName=schedules.getPlaceName();
//        this.details= schedules.getDetails();
        this.referenceURL = schedules.getReferenceURL();
    }

    // DTO로 직접 업데이트
    public void update(SchedulesRequestDto requestDto){
//        this.tripDate = updateTripDate(tripDate, requestDto);
        this.schedulesCategory = requestDto.getSchedulesCategory();
//        this.startTime = requestDto.getStartTime();
//        this.endTime = requestDto.getEndTime();
        this.timeSpent = requestDto.getTimeSpent();
        this.costs = requestDto.getCosts();
        this.contents = requestDto.getContents();
        this.placeName = requestDto.getPlaceName();
//        this.details = requestDto.getDetails();
        this.referenceURL = requestDto.getReferenceURL();
    }

}
