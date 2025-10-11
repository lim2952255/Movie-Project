package com.limhs.movie_project.repository.like;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.comment.Comment;
import com.limhs.movie_project.domain.like.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);

    Optional<CommentLike> findByCommentIdAndUser(Long CommentId, User user);

    void deleteByComment(Comment comment);
}
