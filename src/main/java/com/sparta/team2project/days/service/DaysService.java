package com.sparta.team2project.days.service;

import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.days.entity.Days;
import com.sparta.team2project.days.repository.DaysRepository;
import com.sparta.team2project.posts.dto.DayRequestDto;
import com.sparta.team2project.posts.dto.DayResponseDto;
import com.sparta.team2project.posts.dto.DaysOnlyRequestDto;
import com.sparta.team2project.schedules.dto.SchedulesRequestDto;
import com.sparta.team2project.schedules.dto.SchedulesResponseDto;
import com.sparta.team2project.schedules.entity.Schedules;
import com.sparta.team2project.schedules.repository.SchedulesRepository;
import com.sparta.team2project.schedules.service.SchedulesService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DaysService {
    private final DaysRepository daysRepository;
    private final UserRepository userRepository;
    private final SchedulesRepository schedulesRepository;

    // ChosenDate와 스케줄 전체를 수정하는 메서드
    @Transactional
    public DayResponseDto updateDays(Long daysId, Users users, DaysOnlyRequestDto daysOnlyRequestDto) {
        Users existUser = checkUser(users); // 유저 확인
        checkAuthority(existUser, users);         // 권한 확인
        Days days = findDays(daysId); // 해당 날짜계획 찾기
        if(
                // 수정하려는 스케줄의 날짜가 범위내 있는지 확인
                (daysOnlyRequestDto.getChosenDate().isBefore(days.getPosts().getEndDate())
                        || daysOnlyRequestDto.getChosenDate().isEqual(days.getPosts().getEndDate())) &&
                (daysOnlyRequestDto.getChosenDate().isAfter(days.getPosts().getStartDate())
                        || daysOnlyRequestDto.getChosenDate().isEqual(days.getPosts().getStartDate()))

        ){
            // 범위내 있으면 스케줄 업데이트
            days.updateDays(daysOnlyRequestDto); //선택 날짜계획 업데이트
            return new DayResponseDto(days); // ResponseDto에 실어서 반환
        }
            // 없으면 날짜 불량 에러 출력
        else{
            throw new CustomException(ErrorCode.DATE_NOT_VALID);
        }
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
