package com.limhs.movie_project.repository.like;

import com.limhs.movie_project.domain.like.Like;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.post.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Optional<Like> findByPostAndUser(Post post, User user);

    //@EntityGraph(attributePaths = {"post"})
    List<Like> findByPostIdIn(List<Long> postIds);
}
