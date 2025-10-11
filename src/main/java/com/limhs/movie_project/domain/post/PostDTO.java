package com.limhs.movie_project.domain.post;

import com.limhs.movie_project.domain.like.Like;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.comment.Comment;
import com.limhs.movie_project.domain.movie.Movie;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class PostDTO {

    public PostDTO(Post post) {
        this.comments = post.getComments();
        this.likes = post.getLikes();
        this.user = post.getUser();
        this.movie = post.getMovie();
        this.id = post.getId();
        this.title = post.getTitle();
        this.createdTime = post.getCreatedTime();
        this.viewCount = post.getViewCount();
    }

    private List<Comment> comments = new ArrayList<>();

    private List<Like> likes = new ArrayList<>();

    private User user;

    private Movie movie;

    private Long id;

    private String title;

    private LocalDateTime createdTime;

    private Long viewCount;

    public int getLikeCount() {
        return likes != null ? likes.size() : 0;
    }

    public int getCommentCount(){
        return comments != null ? comments.size() : 0;
    }
}
