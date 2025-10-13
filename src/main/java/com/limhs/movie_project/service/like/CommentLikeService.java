package com.limhs.movie_project.service.like;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.comment.Comment;
import com.limhs.movie_project.domain.comment.CommentDTO;
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
        Comment comment = findComment(commentId);
        User user = userService.getUser(session);

        CommentLike commentLike = new CommentLike();
        commentLike.setCommentLike(user,comment);

        commentLikeRepository.save(commentLike);
    }

    @Transactional
    public void deleteCommentLike(Long commentId,HttpSession session) {
        Comment comment = findComment(commentId);
        User user = userService.getUser(session);

        CommentLike commentLike = findCommentLike(comment, user);
        commentLike.deleteCommentLike();
    }

    @Transactional
    public Comment findComment(Long commentId){
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()){
            throw new RuntimeException();
        }
        return findComment.get();
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
    public boolean userLikesComment(User user, CommentDTO commentDTO){
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByCommentIdAndUser(commentDTO.getId(), user);
        if(findCommentLike.isEmpty()){
            return false;
        }
        return true;
    }
}
