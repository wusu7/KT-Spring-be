package com.techup.spring.spring_be.repository;

import com.techup.spring.spring_be.domain.Comment;
import com.techup.spring.spring_be.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);
}
