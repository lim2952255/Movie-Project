package com.limhs.movie_project.domain.comment;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.like.CommentLike;
import com.limhs.movie_project.domain.post.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class CommentDTO {
    public CommentDTO(Comment comment) {
        this.commentLikes = comment.getCommentLikes();
        this.id = comment.getId();
        this.user = comment.getUser();
        this.post = comment.getPost();
        this.content = comment.getContent();
        this.createdTime = comment.getCreatedTime();
    }
    private List<CommentLike> commentLikes = new ArrayList<>();

    private Long id;

    private User user;

    private Post post;

    private String content;

    private LocalDateTime createdTime;

    private boolean userLike;

    public int getCommentLikeCount(){
        return commentLikes != null ? commentLikes.size() : 0;
    }
}
