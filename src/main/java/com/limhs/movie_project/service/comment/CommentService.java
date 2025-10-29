package com.limhs.movie_project.service.comment;

import com.limhs.movie_project.domain.like.CommentLike;
import com.limhs.movie_project.domain.movie.MovieCardDTO;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.comment.Comment;
import com.limhs.movie_project.domain.post.Post;
import com.limhs.movie_project.repository.comment.CommentRepository;
import com.limhs.movie_project.repository.post.PostRepository;
import com.limhs.movie_project.service.post.PostService;
import com.limhs.movie_project.service.user.UserService;
import com.limhs.movie_project.service.like.CommentLikeService;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter @Setter
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentLikeService commentLikeService;
    private final PostService postService;
    private Pageable pageable;

    @Transactional
    public void saveComment(Comment comment, Long postId){
        Post post = postService.findPostForUpdate(postId);
        User user = userService.getUserForUpdate();

        comment.setComment(user,post);
        commentRepository.save(comment);
    }
    
    // Comment를 페이지 단위로 조회
    @Transactional(readOnly = true)
    public Page<Comment> findComment(Long postId, int pageNumber){
        //Pageable
        pageable = PageRequest.of(pageNumber, 10);

        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        for (Comment comment : comments) {
            comment.setCommentLikeSize(comment.getCommentLikes().size());
        }
        return comments;
    }
    
    // 사용자가 댓글에 좋아요를 눌렀는지 확인하기
    @Transactional
    public void userLikesComment(List<Comment> comments, User user){

        for (Comment comment : comments) {
            comment.setUserLike(false);
            for (CommentLike commentLike : comment.getCommentLikes()) {
                if(commentLike.getUser().getUserId().equals(user.getUserId())){
                    comment.setUserLike(true);
                    break;
                }
            }
        }
    }
    
    // 읽기 전용 조회 메서드
    @Transactional(readOnly = true)
    public Comment findCommentForRead(Long commentId){
        return commentRepository.findCommentForRead(commentId).orElse(null);
    }
    
    // 수저 전용 조회 메서드
    @Transactional
    public Comment findCommentForUpdate(Long commentId){
        return commentRepository.findCommentForUpdate(commentId).orElse(null);
    }

    @Transactional
    public Comment updateComment(Comment comment, Long commentId){
        Comment updateComment = findCommentForUpdate(commentId);
        updateComment.setContent(comment.getContent());
        updateComment.setUpdatedTime(LocalDateTime.now());

        return updateComment;
    }

    @Transactional
    public void deleteComment(Long commentId){
        Comment comment = findCommentForUpdate(commentId);
        comment.deleteComment();
    }

    @Transactional(readOnly = true)
    public long getTotalElements(Page<Comment> commentsList){
        return commentsList.getTotalElements();
    }

    @Transactional(readOnly = true)
    public int getCurrentElements(Page<Comment> commentsList){
        return commentsList.getNumber() * commentsList.getSize() + commentsList.getNumberOfElements();
    }
}
