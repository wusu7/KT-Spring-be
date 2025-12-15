package com.techup.spring.spring_be.service;

import com.techup.spring.spring_be.domain.Favorite;
import com.techup.spring.spring_be.domain.Post;
import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.repository.FavoriteRepository;
import com.techup.spring.spring_be.repository.PostRepository;
import com.techup.spring.spring_be.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /** 좋아요 토글 (추가/취소) */
    @Transactional
    public FavoriteToggleResponse toggleFavorite(Long userId, Long postId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        return favoriteRepository.findByUserAndPost(user, post)
                .map(existing -> {
                    favoriteRepository.delete(existing);
                    long count = favoriteRepository.countByPost(post);
                    return new FavoriteToggleResponse(false, count);
                })
                .orElseGet(() -> {
                    favoriteRepository.save(new Favorite(user, post));
                    long count = favoriteRepository.countByPost(post);
                    return new FavoriteToggleResponse(true, count);
                });
    }

    /** 좋아요 상태/카운트 조회 */
    @Transactional(readOnly = true)
    public FavoriteStatusResponse getFavoriteStatus(Long postId, Long userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        long count = favoriteRepository.countByPost(post);

        boolean favorited = false;
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
            favorited = favoriteRepository.existsByUserAndPost(user, post);
        }

        return new FavoriteStatusResponse(favorited, count);
    }

    public record FavoriteToggleResponse(boolean favorited, long favoriteCount) {}

    public record FavoriteStatusResponse(boolean favorited, long favoriteCount) {}
}