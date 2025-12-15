package com.techup.spring.spring_be.controller;

import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.common.ApiResponse;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserRepository userRepository;

    private Long getCurrentUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("회원 정보를 찾을 수 없습니다."));
        return user.getId();
    }

    @PostMapping("/{postId}/favorite")
    public ApiResponse<FavoriteService.FavoriteToggleResponse> toggleFavorite(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = getCurrentUserId(userDetails);
        var res = favoriteService.toggleFavorite(userId, postId);
        return ApiResponse.ok("좋아요 토글 성공", res);
    }

    @GetMapping("/{postId}/favorite")
    public ApiResponse<FavoriteService.FavoriteStatusResponse> getFavoriteStatus(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = null;
        if (userDetails != null) {
            userId = getCurrentUserId(userDetails);
        }
        var res = favoriteService.getFavoriteStatus(postId, userId);
        return ApiResponse.ok("좋아요 상태 조회 성공", res);
    }
}
