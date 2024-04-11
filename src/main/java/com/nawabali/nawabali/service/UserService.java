package com.nawabali.nawabali.service;

import com.nawabali.nawabali.constant.Address;
import com.nawabali.nawabali.constant.UserRankEnum;
import com.nawabali.nawabali.constant.UserRoleEnum;
import com.nawabali.nawabali.domain.User;
import com.nawabali.nawabali.domain.image.ProfileImage;
import com.nawabali.nawabali.dto.SignupDto;
import com.nawabali.nawabali.dto.UserDto;
import com.nawabali.nawabali.exception.CustomException;
import com.nawabali.nawabali.exception.ErrorCode;
import com.nawabali.nawabali.repository.ProfileImageRepository;
import com.nawabali.nawabali.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileImageRepository profileImageRepository;


    @Transactional
    public ResponseEntity<SignupDto.SignupResponseDto> signup(SignupDto.SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();
        String rawPassword = requestDto.getPassword();

        // 비밀번호 일치 검증
        if (!rawPassword.equals(requestDto.getConfirmPassword())) {
            throw new CustomException(ErrorCode.MISMATCH_PASSWORD);

        }

        String password = passwordEncoder.encode(rawPassword);


        // 관리자 권한 부여
        UserRoleEnum role = UserRoleEnum.USER;
//        if(requestDto.isAdmin()){
//            role = UserRoleEnum.ADMIN;
//        }

        // 프론트엔드로부터 받은 주소 정보를 사용하여 Address 객체 생성
        Address address = new Address(
                requestDto.getCity(),
                requestDto.getDistrict()
        );

        User user = User.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .role(role)
                .address(address)
                .rank(UserRankEnum.RESIDENT)
                .build();

        ProfileImage profileImage = new ProfileImage(user);

        userRepository.save(user);
        profileImageRepository.save(profileImage);
        User responseUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        return ResponseEntity.ok(new SignupDto.SignupResponseDto(responseUser.getId()));
    }


    public UserDto.UserInfoResponseDto getUserInfo(User user) {
        User existUser = getUserId(user.getId());

        Long localCount = 1L; // 물어볼것, 수정해야할 부분
        Long likesCount = 1L;

        return UserDto.UserInfoResponseDto.builder()
                .id(existUser.getId())
                .email(existUser.getEmail())
                .nickname(existUser.getNickname())
                .rank(existUser.getRank())
                .city(existUser.getAddress().getCity())
                .district(existUser.getAddress().getDistrict())
                .localCount(localCount)
                .likesCount(likesCount)
                .build();
    }

    @Transactional
    public UserDto.UserInfoResponseDto updateUserInfo(User user, UserDto.UserInfoRequestDto requestDto) {
        User existUser = getUserId(user.getId());

        existUser.update(requestDto);
        return new UserDto.UserInfoResponseDto(existUser);
    }

    @Transactional
    public ResponseEntity<UserDto.deleteResponseDto> deleteUserInfo(User user) {
        User existUser = getUserId(user.getId());

        userRepository.delete(existUser);
        return ResponseEntity.ok(new UserDto.deleteResponseDto());
    }

    public boolean checkNickname(String nickname) {
        User duplicatedUser = userRepository.findByNickname(nickname);
        return duplicatedUser == null;
    }


    public User getUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
