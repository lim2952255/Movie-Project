package com.limhs.movie_project.domain.like;

import com.limhs.movie_project.domain.mappedSuperClass.BaseEntity;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "likes")
@Getter @Setter
public class Like extends BaseEntity {
    public Like() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
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
}
