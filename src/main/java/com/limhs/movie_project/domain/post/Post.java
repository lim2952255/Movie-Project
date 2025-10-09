package com.limhs.movie_project.domain.post;

import com.limhs.movie_project.domain.User;
import com.limhs.movie_project.domain.movie.Movie;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Post {
    public Post() {
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 100, message = "제목은 100자를 넘어갈 수 없습니다.")
    private String title;

    @NotBlank(message = "본문은 필수입니다.")
    @Size(max = 2000, message = "본문 내용은 2000자를 넘을 수 없습니다.")
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdTime;

    private Long viewCount = 0L;

    public void setPost(Movie movie, User user){
        if(this.movie != null && this.movie != movie){
            this.movie.getPosts().remove(this);
        }

        this.movie = movie;

        if(movie != null && !movie.getPosts().contains(this)){
            movie.getPosts().add(this);
        }

        if(this.user != null && this.user != user){
            this.user.getPosts().remove(this);
        }

        this.user = user;

        if(user != null && !user.getPosts().contains(this)){
            user.getPosts().add(this);
        }
    }

    public void deletePost(){
        this.movie.getPosts().remove(this);
        this.user.getPosts().remove(this);
    }
}
