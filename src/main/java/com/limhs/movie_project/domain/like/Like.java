package com.limhs.movie_project.domain.like;

import com.limhs.movie_project.domain.mappedSuperClass.BaseEntity;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "likes")
@Getter @Setter
public class Like extends BaseEntity {
    public Like() {
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public void setLike(Post post, User user){
        if(this.post != null && this.post != post){
            this.post.getLikes().remove(this);
        }

        this.post = post;

        if(post != null && !post.getLikes().contains(this)){
            post.getLikes().add(this);
        }

        if(this.user != null && this.user != user){
            this.user.getLikes().remove(this);
        }

        this.user = user;

        if(user != null && !user.getLikes().contains(this)){
            user.getLikes().add(this);
        }
    }

    public void deleteLike(){
        this.post.getLikes().remove(this);
        this.user.getLikes().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Like like)) return false;
        return Objects.equals(this.getUser().getId(), like.getUser().getId()) &&
                Objects.equals(this.getPost().getId(), like.getPost().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, user);
    }
}
