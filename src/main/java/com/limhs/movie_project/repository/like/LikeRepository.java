package com.limhs.movie_project.repository.like;

import com.limhs.movie_project.domain.like.Like;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Optional<Like> findByPostAndUser(Post post, User user);

    void deleteByPost(Post post);
}
