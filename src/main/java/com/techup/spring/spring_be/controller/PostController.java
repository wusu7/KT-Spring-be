package com.techup.spring.spring_be.controller;

import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.common.ApiResponse;
import com.techup.spring.spring_be.dto.post.PostCreateRequest;
import com.techup.spring.spring_be.dto.post.PostResponse;
import com.techup.spring.spring_be.dto.post.PostUpdateRequest;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    // 로그인 회원의 userId 가져오는 헬퍼
    private Long getCurrentUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("회원 정보를 찾을 수 없습니다."));
        return user.getId();
    }

    /** 게시글 생성 */
    @PostMapping
    public ApiResponse<PostResponse> createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PostCreateRequest request
    ) {
        Long userId = getCurrentUserId(userDetails);
        PostResponse res = postService.createPost(userId, request);
        return ApiResponse.ok("게시글 생성 성공", res);
    }

    /** 단건 조회 */
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse res = postService.getPost(postId);
        return ApiResponse.ok("게시글 조회 성공", res);
    }

    /** 커뮤니티별 목록 */
    @GetMapping("/community/{communityId}")
    public ApiResponse<Page<PostResponse>> getPostsByCommunity(
            @PathVariable Long communityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PostResponse> res = postService.getPostsByCommunity(communityId, page, size);
        return ApiResponse.ok("게시글 목록 조회 성공", res);
    }

    /** 수정 */
    @PutMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        Long userId = getCurrentUserId(userDetails);
        PostResponse res = postService.updatePost(postId, userId, request);
        return ApiResponse.ok("게시글 수정 성공", res);
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = getCurrentUserId(userDetails);
        postService.deletePost(postId, userId);
        return ApiResponse.ok("게시글 삭제 성공");
    }
}
