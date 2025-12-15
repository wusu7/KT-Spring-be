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

    public record FavoriteToggleResponse(boolean favorited, long favoriteCount) {}
}
