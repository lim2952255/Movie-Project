package com.limhs.movie_project.domain.post;

import com.limhs.movie_project.domain.User;
import com.limhs.movie_project.domain.movie.Movie;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PostDTO {

    public PostDTO(Post post) {
        this.user = post.getUser();
        this.movie = post.getMovie();
        this.id = post.getId();
        this.title = post.getTitle();
        this.createdTime = post.getCreatedTime();
        this.viewCount = post.getViewCount();
    }

    private User user;

    private Movie movie;

    private Long id;

    private String title;

    private LocalDateTime createdTime;

    private Long viewCount;
}
