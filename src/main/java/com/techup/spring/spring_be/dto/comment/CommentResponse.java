package com.techup.spring.spring_be.dto.comment;

import com.techup.spring.spring_be.domain.Comment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponse {

    private final Long id;
    private final Long postId;
    private final Long userId;

    private final String authorName;
    private final String authorProfileImage;

    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentResponse(Comment comment) {

        this.id = comment.getId();
        this.postId = comment.getPost().getId();
        this.userId = comment.getUser().getId();

        this.authorName = comment.getUser().getName();
        this.authorProfileImage = comment.getUser().getProfileImage();

        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt(); // BaseEntity에 updatedAt이 있으면 OK
    }
}
