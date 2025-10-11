package com.limhs.movie_project.repository.post;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findByMovie_MovieId(int movieId, Pageable pageable);
    Page<Post> findByUser_UserId(String userId, Pageable pageable);
    Page<Post> findByLikes_User(User user, Pageable pageable);
}
