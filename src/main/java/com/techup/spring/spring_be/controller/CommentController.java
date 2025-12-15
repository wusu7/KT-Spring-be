package com.techup.spring.spring_be.controller;

import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.comment.CommentCreateRequest;
import com.techup.spring.spring_be.dto.comment.CommentResponse;
import com.techup.spring.spring_be.dto.comment.CommentUpdateRequest;
import com.techup.spring.spring_be.dto.common.ApiResponse;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    // 로그인 회원의 userId 가져오는 헬퍼 (PostController와 동일)
    private Long getCurrentUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("회원 정보를 찾을 수 없습니다."));
        return user.getId();
    }

    /** 댓글 생성 */
    @PostMapping
    public ApiResponse<CommentResponse> create(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        Long userId = getCurrentUserId(userDetails);
        CommentResponse res = commentService.createComment(userId, postId, request);
        return ApiResponse.ok("댓글 생성 성공", res);
    }

    /** 댓글 목록(페이징) */
    @GetMapping
    public ApiResponse<Page<CommentResponse>> list(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CommentResponse> res = commentService.getCommentsByPost(postId, page, size);
        return ApiResponse.ok("댓글 목록 조회 성공", res);
    }

    /** 댓글 수정 */
    @PutMapping("/{commentId}")
    public ApiResponse<CommentResponse> update(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        Long userId = getCurrentUserId(userDetails);
        CommentResponse res = commentService.updateComment(commentId, userId, request);
        return ApiResponse.ok("댓글 수정 성공", res);
    }

    /** 댓글 삭제 */
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> delete(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = getCurrentUserId(userDetails);
        commentService.deleteComment(commentId, userId);
        return ApiResponse.ok("댓글 삭제 성공");
    }
}