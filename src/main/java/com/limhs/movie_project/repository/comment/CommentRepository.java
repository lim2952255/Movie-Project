package com.limhs.movie_project.repository.comment;

import com.limhs.movie_project.domain.comment.Comment;
import com.limhs.movie_project.domain.post.Post;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @EntityGraph(attributePaths = {"user", "commentLikes"})
    //@EntityGraph(attributePaths = {"user"})
    @QueryHints(@QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Page<Comment> findByPostId(Long postId, Pageable pageable);

    @QueryHints(@QueryHint(name = "org.hibernate.readOnly", value = "true"))
    @Query("select c from Comment c where c.id = :id")
    Optional<Comment> findCommentForRead(@Param("id") Long id);

    @Query("select c from Comment c where c.id = :id")
    Optional<Comment> findCommentForUpdate(@Param("id") Long id);
}
