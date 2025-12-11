package com.techup.spring.spring_be.repository;

import com.techup.spring.spring_be.domain.Favorite;
import com.techup.spring.spring_be.domain.Post;
import com.techup.spring.spring_be.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserAndPost(User user, Post post);

    Optional<Favorite> findByUserAndPost(User user, Post post);

    long countByPost(Post post);
}
