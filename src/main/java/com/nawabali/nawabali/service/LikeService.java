package com.nawabali.nawabali.service;

import com.nawabali.nawabali.constant.LikeCategoryEnum;
import com.nawabali.nawabali.domain.Like;
//import com.nawabali.nawabali.domain.LocalLike;
import com.nawabali.nawabali.domain.Post;
import com.nawabali.nawabali.domain.User;
import com.nawabali.nawabali.dto.LikeDto;
import com.nawabali.nawabali.exception.CustomException;
import com.nawabali.nawabali.exception.ErrorCode;
import com.nawabali.nawabali.repository.LikeRepository;
//import com.nawabali.nawabali.repository.LocalLikeRepository;
import com.nawabali.nawabali.repository.NotificationRepository;
import com.nawabali.nawabali.repository.PostRepository;
import com.nawabali.nawabali.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j (topic = "LikeService 로그")
public class LikeService {

    private final LikeRepository likeRepository;
//    private final LocalLikeRepository localLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    // 좋아요 수정
    public LikeDto.responseDto toggleLike(Long postId, String username) {

        // 본인 인증
        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new CustomException(ErrorCode.UNAUTHORIZED_MEMBER));

        // 게시물 가져오기
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 해당 게시물에 좋아요를 눌렀는지 확인
        Like findLike = likeRepository.findByUserIdAndPostIdAndLikeCategoryEnum(user.getId(), postId, LikeCategoryEnum.LIKE);

        if (findLike != null && findLike.isStatus()){
            // 좋아요를 이미 눌렀다면, 좋아요 내역 삭제
            likeRepository.delete(findLike);
            // response 보내기
            return LikeDto.responseDto.builder()
                    .likeId(findLike.getId())
                    .userId(user.getId())
                    .postId(postId)
                    .likeCategoryEnum(findLike.getLikeCategoryEnum())
                    .status(findLike.isStatus())
                    .message("좋아요 취소 되었습니다.")
                    .build();
        }

        else{
            // 내역에 없디면 추가.
            findLike = Like.builder()
                    .user(user)
                    .post(post)
                    .likeCategoryEnum(LikeCategoryEnum.LIKE)
                    .status(true)
                    .build();

            likeRepository.save(findLike);

            notificationService.notifyLike(postId,user.getId());

        }
        // response 보내기
        return LikeDto.responseDto.builder()
                .likeId(findLike.getId())
                .userId(user.getId())
                .postId(postId)
                .likeCategoryEnum(findLike.getLikeCategoryEnum())
                .status(findLike.isStatus())
                .message("좋아요 되었습니다.")
                .build();
    }

    public LikeDto.responseDto toggleLocalLike(Long postId, String username) {
        // 본인 인증
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_MEMBER));

        // 게시물 가져오기
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 해당 지역의 회원인지 확인
//        if(!isMatchDistrict(user, post)){
//            throw new CustomException(ErrorCode.MISMATCH_ADDRESS);
//        }

        // 해당 게시물에 로컬좋아요를 눌렀는지 확인
        Like findLocalLike = likeRepository.findByUserIdAndPostIdAndLikeCategoryEnum(user.getId(), postId, LikeCategoryEnum.LOCAL_LIKE);
        if(findLocalLike != null && findLocalLike.isStatus()){
            // 누른 상태라면 삭제
            likeRepository.delete(findLocalLike);

            return LikeDto.responseDto.builder()
                    .likeId(findLocalLike.getId())
                    .userId(user.getId())
                    .postId(postId)
                    .likeCategoryEnum(findLocalLike.getLikeCategoryEnum())
                    .status(findLocalLike.isStatus())
                    .message("동네 좋아요 취소 되었습니다.")
                    .build();
        }

        else{
            // 내역에 없디면 추가.
            findLocalLike = Like.builder()
                    .user(user)
                    .post(post)
                    .likeCategoryEnum(LikeCategoryEnum.LOCAL_LIKE)
                    .status(true)
                    .build();

            likeRepository.save(findLocalLike);

            notificationService.notifyLocalLike(postId, user.getId());

            return LikeDto.responseDto.builder()
                    .likeId(findLocalLike.getId())
                    .userId(user.getId())
                    .postId(postId)
                    .likeCategoryEnum(findLocalLike.getLikeCategoryEnum())
                    .status(findLocalLike.isStatus())
                    .message("동네 좋아요 되었습니다.")
                    .build();
        }
    }

    private boolean isMatchDistrict(User user, Post post){
        String userAddress = user.getAddress().getDistrict();
        String postAddress = post.getTitle();

        return userAddress.equals(postAddress);
    }

    // 좋아요 취소
//    public LikeDto.responseDto deleteLike(Long postId, LikeDto.requestDto dto, String username) {
//
//        // 본인인증
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(()-> new CustomException(ErrorCode.UNAUTHORIZED_MEMBER));
//
//        // 포스트 찾기
//        Post post = postRepository.findById(postId)
//                .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
//
//        Like findLike = likeRepository.findByUserIdAndPostIdAndLikeCategory(user.getId(), postId, dto.getLikeCategory())
//                .orElseThrow(()-> new CustomException(ErrorCode.LIKE_NOT_FOUND));
//
//        if (!findLike.isStatus() || findLike.getLikesCount() < 0) {
//            throw new CustomException(ErrorCode.DUPLICATE_LIKE_FALSE);
//        }
//
//        likeRepository.delete(findLike);
//
//        // response 보내기
//        return LikeDto.responseDto.builder()
//                .likeId(findLike.getId())
//                .userId(user.getId())
//                .postId(postId)
//                .likeCategory(findLike.getLikeCategory())
//                .status(findLike.isStatus())
//                .message("좋아요가 취소되었습니다.")
//                .build();
//    }

    // LocalLike 좋아요 추가
//    public LikeDto.responseDto createLocalLike(Long postId, LikeDto.requestDto dto, String username) {
//
//        // 본인 인증
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(()-> new CustomException(ErrorCode.UNAUTHORIZED_MEMBER));
//
//        // 게시물 가져오기
//        Post post = postRepository.findById(postId)
//                .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
//
//        // 좋아요 했는지 확인
//        if (!dto.isStatus()) {
//            throw new CustomException(ErrorCode.DUPLICATE_LIKE_TRUE);
//        }
//
//        // 좋아요 카운트 & 저장
//        LocalLike localLike = new LocalLike();
//
//        if (localLike.getLocalLikesCount() == null) {
//            LocalLike.builder()
//                    .localLikesCount(0L);
//        }
//
//        LocalLike localLike1 = LocalLike.builder()
//                .user(user)
//                .post(post)
//                .status(true)
//                .localLikesCount(localLike.getLocalLikesCount() + 1)
//                .build();
//
//        localLikeRepository.save(localLike1);
//
//        // response 보내기
//        return LikeDto.responseDto.builder()
//                .likeId(localLike.getId())
//                .userId(user.getId())
//                .postId(postId)
//                .status(localLike.isStatus())
//                .message("좋아요 되었습니다.")
//                .build();
//
//    }
//
//    // LocalLike 좋아요 취소
//    public LikeDto.responseDto deleteLocalLike(Long postId, LikeDto.requestDto dto, String username) {
//
//        // 본인 인증
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(()-> new CustomException(ErrorCode.UNAUTHORIZED_MEMBER));
//
//        // 게시물 가져오기
//        Post post = postRepository.findById(postId)
//                .orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
//
//        // 좋아요 했으면 삭제하고 저장하기
//        LocalLike localLike = new LocalLike();
//
//        if (dto.isStatus() || localLike.getLocalLikesCount() < 0) {
//            throw new CustomException(ErrorCode.DUPLICATE_LIKE_FALSE);
//        }
//
//        LocalLike localLike1 = LocalLike.builder()
//                .user(user)
//                .post(post)
//                .status(true)
//                .localLikesCount(localLike.getLocalLikesCount() - 1)
//                .build();
//
//        localLikeRepository.delete(localLike1);
//
//        // response 보내기
//        return LikeDto.responseDto.builder()
//                .likeId(localLike.getId())
//                .userId(user.getId())
//                .postId(postId)
//                .status(localLike.isStatus())
//                .message("좋아요가 취소되었습니다.")
//                .build();
//    }
}