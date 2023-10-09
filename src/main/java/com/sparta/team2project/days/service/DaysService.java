package com.sparta.team2project.days.service;

import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.days.entity.Days;
import com.sparta.team2project.days.repository.DaysRepository;
import com.sparta.team2project.posts.dto.DayRequestDto;
import com.sparta.team2project.posts.dto.DayResponseDto;
import com.sparta.team2project.schedules.dto.SchedulesRequestDto;
import com.sparta.team2project.schedules.dto.SchedulesResponseDto;
import com.sparta.team2project.schedules.entity.Schedules;
import com.sparta.team2project.users.UserRepository;
import com.sparta.team2project.users.UserRoleEnum;
import com.sparta.team2project.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Transactional
public class DaysService {
    private final DaysRepository daysRepository;
    private final UserRepository userRepository;

    // ChosenDate만 수정하는 메서드
    @Transactional
    public DayResponseDto updateChosenDate(Long daysId, Users users, DayRequestDto dayRequestDto) {
        Users existUser = checkUser(users); // 유저 확인
        checkAuthority(existUser, users);         // 권한 확인
        Days days = findDays(daysId); // 해당 날짜계획 찾기
        days.updateChosenDate(dayRequestDto); //선택날짜 정보 업데이트
        return new DayResponseDto(days); // ResponseDto에 실어서 반환
    }

    // ChosenDate와 스케줄 전체를 수정하는 메서드
    @Transactional
    public DayResponseDto updateDays(Long daysId, Users users, DayRequestDto dayRequestDto) {
        Users existUser = checkUser(users); // 유저 확인
        checkAuthority(existUser, users);         // 권한 확인
        Days days = findDays(daysId); // 해당 날짜계획 찾기
        days.updateDays(dayRequestDto); //선택날짜 정보 및 스케줄 업데이트
        return new DayResponseDto(days); // ResponseDto에 실어서 반환
    }


    // 유저 유효 확인 메서드
    private Users checkUser (Users users) {
        return userRepository.findByEmail(users.getEmail()).
                orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_MATCH));
    }

    // 유저 권한 검사 메서드
    private void checkAuthority(Users existUser,Users users){
        if (!existUser.getUserRole().equals(UserRoleEnum.ADMIN)&&!existUser.getEmail().equals(users.getEmail())) {
            throw new CustomException(ErrorCode.NOT_ALLOWED);
        }
    }

    // Days를 Repository에서 찾는 메서드
    private Days findDays(Long id) {
        return daysRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.ID_NOT_MATCH));
    }
}
