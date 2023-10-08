package com.sparta.team2project.schedules.service;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.posts.dto.DayRequestDto;
import com.sparta.team2project.posts.dto.DayResponseDto;
import com.sparta.team2project.posts.repository.PostsRepository;
import com.sparta.team2project.schedules.dto.SchedulesRequestDto;
import com.sparta.team2project.schedules.dto.SchedulesResponseDto;
import com.sparta.team2project.schedules.entity.Schedules;
import com.sparta.team2project.schedules.repository.SchedulesRepository;
import com.sparta.team2project.users.UserRepository;
import com.sparta.team2project.users.UserRoleEnum;
import com.sparta.team2project.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SchedulesService {

    // 사용할 Repository 선언
    private final SchedulesRepository schedulesRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
//    public List<DayResponseDto> createSchedules(Long postId, Long daysId, UserDetailsImpl userDetails, DayRequestDto requestDto) {
//
//    }

    // Schedules 조회 메서드 (권한 확인 없음)
    public SchedulesResponseDto getSchedules(Long schedulesId) {
        Schedules schedules = findSchedules(schedulesId);
        return new SchedulesResponseDto(schedules);
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

    // schedules를 Repository에서 찾는 메서드
    private Schedules findSchedules(Long id) {
        return schedulesRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.ID_NOT_MATCH));
    }


    // schedules 수정 메서드
    @Transactional
    public SchedulesResponseDto updateSchedules(Long schedulesId, Users users, SchedulesRequestDto requestDto) {
        Users existUser = checkUser(users); // 유저 확인
        checkAuthority(existUser, users);         // 권한 확인
        Schedules schedules = findSchedules(schedulesId); // 해당 세부일정 찾기
        schedules.update(schedules.getDays(), requestDto); //세부일정 업데이트
        return new SchedulesResponseDto(schedules); // ResponseDto에 실어서 반환
    }

    // schedules 삭제 메서드
    public MessageResponseDto deleteSchedules(Long schedulesId, Users users) {
        Users existUser = checkUser(users); // 유저 확인
        checkAuthority(existUser, users);         // 권한 확인
        Schedules schedules = findSchedules(schedulesId);
        schedulesRepository.delete(schedules);
        MessageResponseDto messageResponseDto = new MessageResponseDto("삭제가 되었습니다.", 200);
        return messageResponseDto;
    }
}
