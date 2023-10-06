package com.sparta.team2project.posts.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int likeNum;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PostCategory postCategory;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;


    public Posts(int likeNum, String contents, String title, PostCategory postCategory, LocalDate startDate, LocalDate endDate) {//user 추가
        this.likeNum = likeNum;
        this.contents = contents;
        this.title = title;
        this.postCategory = postCategory;
        this.startDate = startDate;
        this.endDate = endDate;
        //this.users = users;
    }
}
