package com.limhs.movie_project.service.like;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.comment.Comment;
import com.limhs.movie_project.domain.comment.CommentDTO;
import com.limhs.movie_project.domain.like.CommentLike;
import com.limhs.movie_project.repository.comment.CommentRepository;
import com.limhs.movie_project.repository.like.CommentLikeRepository;
import com.limhs.movie_project.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public void setCommentLike(Long commentId, HttpServletRequest request, HttpServletResponse response){
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()){
            throw new RuntimeException();
        }
        Comment comment = findComment.get();
        User user = userService.getUser(request, response);

        CommentLike commentLike = new CommentLike();
        commentLike.setCommentLike(user,comment);

        commentLikeRepository.save(commentLike);
    }

    @Transactional
    public void deleteCommentLike(Long commentId, HttpServletRequest request, HttpServletResponse response) {
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()){
            throw new RuntimeException();
        }
        Comment comment = findComment.get();
        User user = userService.getUser(request, response);

        Optional<CommentLike> findCommentLike = commentLikeRepository.findByCommentAndUser(comment, user);
        if(findCommentLike.isEmpty()){
            throw new RuntimeException();
        }
        CommentLike commentLike = findCommentLike.get();
        commentLike.deleteCommentLike();

        commentLikeRepository.delete(commentLike);
    }

    @Transactional
    public boolean userLikesComment(User user, CommentDTO commentDTO){
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByCommentIdAndUser(commentDTO.getId(), user);
        if(findCommentLike.isEmpty()){
            return false;
        }
        return true;
    }

    @Transactional
    public void deleteByComment(Comment comment){
        commentLikeRepository.deleteByComment(comment);
    }
}
