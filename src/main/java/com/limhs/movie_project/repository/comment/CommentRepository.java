package com.limhs.movie_project.repository.comment;

import com.limhs.movie_project.domain.comment.Comment;
import com.limhs.movie_project.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findByPostId(Long postId, Pageable pageable);
}
