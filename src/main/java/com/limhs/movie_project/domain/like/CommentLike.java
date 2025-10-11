package com.limhs.movie_project.domain.like;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.comment.Comment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class CommentLike {
    public CommentLike() {
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public void setCommentLike(User user, Comment comment){
        if(this.user != null && this.user != user){
            this.user.getCommentLikes().remove(this);
        }
        this.user = user;
        if(this.user != null && !user.getCommentLikes().contains(this)){
            user.getCommentLikes().add(this);
        }

        if(this.comment != null && this.comment != comment){
            this.comment.getCommentLikes().remove(this);
        }
        this.comment = comment;
        if(this.comment != null && !comment.getCommentLikes().contains(this)){
            comment.getCommentLikes().add(this);
        }
    }

    public void deleteCommentLike(){
        if(user != null){
            this.user.getCommentLikes().remove(this);
        }
        if(comment != null){
            this.comment.getCommentLikes().remove(this);
        }
    }
}
