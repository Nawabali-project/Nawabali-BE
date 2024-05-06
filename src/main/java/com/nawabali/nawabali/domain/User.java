package com.nawabali.nawabali.domain;

import com.nawabali.nawabali.constant.Address;
import com.nawabali.nawabali.constant.UserRankEnum;
import com.nawabali.nawabali.constant.UserRoleEnum;
import com.nawabali.nawabali.domain.image.ProfileImage;
import com.nawabali.nawabali.dto.UserDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Embedded
    @Column(nullable = false)
    @NotNull
    private Address address;

    @Column
    private Long kakaoId;

    @Column(columnDefinition = "boolean default false")
    private boolean oauthStatus;

    @Column(nullable = false, name = "user_rank")
    @Enumerated(EnumType.STRING)
    private UserRankEnum rank;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProfileImage profileImage;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user" ,  fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BookMark> bookMarks = new ArrayList<>();

    @Builder
    public User(Long kakaoId, String nickname, String email, String password,
                UserRoleEnum role, Address address, UserRankEnum rank, ProfileImage profileImage, boolean oauthStatus) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.address = address;
        this.rank = rank;
        this.profileImage = profileImage;
        this.oauthStatus = oauthStatus;


        if (profileImage != null) {
            profileImage.setUser(this);
        }
    }

    public void updateKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
    }

    public void update(UserDto.UserInfoRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.address = new Address(requestDto.getCity(), requestDto.getDistrict());
        this.password = requestDto.getPassword();
    }

    public void updateRanks (UserRankEnum rank) {
        this.rank = rank;
    }

    public void updateRank(UserRankEnum userRankEnum){
        if(userRankEnum == UserRankEnum.RESIDENT){
            this.rank = UserRankEnum.NATIVE_PERSON;
        }
        else if(userRankEnum == UserRankEnum.NATIVE_PERSON){
            this.rank = UserRankEnum.LOCAL_ELDER;
        }
    }

    public void updateProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }
}
