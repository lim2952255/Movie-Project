package com.limhs.movie_project.service.comment;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.comment.Comment;
import com.limhs.movie_project.domain.comment.CommentDTO;
import com.limhs.movie_project.domain.post.Post;
import com.limhs.movie_project.repository.comment.CommentRepository;
import com.limhs.movie_project.repository.post.PostRepository;
import com.limhs.movie_project.service.user.UserService;
import com.limhs.movie_project.service.like.CommentLikeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final CommentLikeService commentLikeService;
    private final PostRepository postRepository;
    private final UserService userService;
    private Pageable pageable;

    @Transactional
    public void saveComment(Comment comment, Long postId, HttpServletRequest request, HttpServletResponse response){
        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            throw new RuntimeException("오류 발생");
        }
        Post post = findPost.get();
        User user = userService.getUser(request, response);

        comment.setComment(user,post);
        comment.setCreatedTime(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Transactional
    public Page<CommentDTO> findComment(Long postId, int pageNumber){
        //Pageable
        pageable = PageRequest.of(pageNumber, 10);

        Page<Comment> findComments = commentRepository.findByPostId(postId, pageable);
        Page<CommentDTO> comments = findComments.map(comment -> new CommentDTO(comment));

        return comments;
    }

    @Transactional
    public List<CommentDTO> userLikesComment(List<CommentDTO> comments, User user){

        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentDTO comment : comments) {
            boolean userLikes = commentLikeService.userLikesComment(user, comment);
            comment.setUserLike(userLikes);
            commentDTOList.add(comment);
        }
        return commentDTOList;
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
    public Comment updateComment(Comment comment, Long commentId){
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()){
            throw new RuntimeException();
        }
        Comment updateComment = findComment.get();
        updateComment.setContent(comment.getContent());
        return updateComment;
    }

    @Transactional
    public void deleteComment(Long commentId){
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()){
            throw new RuntimeException();
        }
        Comment comment = findComment.get();

        commentLikeService.deleteByComment(comment);

        comment.deleteComment();
        commentRepository.delete(comment);
    }
}
