package com.limhs.movie_project.service.like;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.comment.Comment;
import com.limhs.movie_project.domain.like.CommentLike;
import com.limhs.movie_project.repository.comment.CommentRepository;
import com.limhs.movie_project.repository.like.CommentLikeRepository;
import com.limhs.movie_project.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Getter @Setter
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    private final UserService userService;

    @Transactional
    public void setCommentLike(Long commentId, HttpSession session){
        Comment comment = findCommentForUpdate(commentId);
        User user = userService.getUserForUpdate(session);

        CommentLike commentLike = new CommentLike();
        commentLike.setCommentLike(user,comment);

        commentLikeRepository.save(commentLike);
    }

    @Transactional
    public void deleteCommentLike(Long commentId,HttpSession session) {
        Comment comment = findCommentForUpdate(commentId);
        User user = userService.getUserForUpdate(session);

        CommentLike commentLike = findCommentLike(comment, user);
        commentLike.deleteCommentLike();
    }

    @Transactional
    public Comment findCommentForUpdate(Long commentId){
        return commentRepository.findCommentForUpdate(commentId).orElse(null);
    }

    @Transactional
    public CommentLike findCommentLike(Comment comment, User user){
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByCommentAndUser(comment, user);
        if(findCommentLike.isEmpty()){
            throw new RuntimeException();
        }
        return findCommentLike.get();
    }
    @Transactional
    public boolean userLikesComment(User user, Comment comment){
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByCommentIdAndUser(comment.getId(), user);
        if(findCommentLike.isEmpty()){
            return false;
        }
        return true;
    }
}
