package com.limhs.movie_project.domain.comment;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.like.CommentLike;
import com.limhs.movie_project.domain.post.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Comment {
    public Comment() {
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> commentLikes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @NotBlank(message = "댓글란은 필수입니다")
    @Size(max = 200, message = "댓글 내용은 200자를 넘을 수 없습니다.")
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdTime;

    public void setComment(User user, Post post){
        if(user != null && this.user != user){
            user.getComments().remove(this);
        }
        this.user = user;

        if(user != null && !user.getComments().contains(this)){
            user.getComments().add(this);
        }

        if(post != null && this.post != post){
            post.getComments().remove(this);
        }
        this.post = post;
        if(post != null && !post.getComments().contains(this)){
            post.getComments().add(this);
        }
    }

    public void deleteComment(){
        if(user != null){
            user.getComments().remove(this);
        }
        if(post != null){
            post.getComments().remove(this);
        }
    }

    public int getCommentLikeCount(){
        return commentLikes != null ? commentLikes.size() : 0;
    }
}
