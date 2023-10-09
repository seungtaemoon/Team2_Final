package com.sparta.team2project.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE Users u SET u.nickName = :nickName, u.profileImg = :profileImg WHERE u.email = :email")
//    void updateProfile(@Param("email") String email, @Param("nickName") String nickName, @Param("profileImg") String profileImg);
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE Users u SET u.password = :password WHERE u.email = :email")
//    void updatePassword(@Param("email") String email, @Param("password") String password);
}
