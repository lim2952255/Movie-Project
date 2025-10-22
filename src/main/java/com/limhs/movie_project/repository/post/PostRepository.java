package com.limhs.movie_project.repository.post;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.post.Post;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findByMovie_MovieId(int movieId, Pageable pageable);
    Page<Post> findByUser_UserId(String userId, Pageable pageable);
    Page<Post> findByLikes_User(User user, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Post p where p.id = :postId")
    @QueryHints({
            // @QueryHint(name = "javax.persistence.lock.timeout", value = "5000")
            @QueryHint(name = "org.hibernate.timeout", value = "5") //타임아웃 설정
    })
    Optional<Post> findByIdWithLock(@Param("postId") Long postId);

}
