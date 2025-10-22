package com.limhs.movie_project.service.comment;

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
    public void saveComment(Comment comment, Long postId, HttpSession session){
        Post post = postService.findPost(postId);
        User user = userService.getUser(session);

        comment.setComment(user,post);
        commentRepository.save(comment);
    }

    @Transactional
    public Page<Comment> findComment(Long postId, int pageNumber){
        //Pageable
        pageable = PageRequest.of(pageNumber, 10);

        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);

        return comments;
    }

    @Transactional
    public List<Comment> userLikesComment(List<Comment> comments, User user){

        List<Comment> commentList = new ArrayList<>();
        for (Comment comment : comments) {
            boolean userLikes = commentLikeService.userLikesComment(user, comment);
            comment.setUserLike(userLikes);
            commentList.add(comment);
        }
        return commentList;
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
        Comment findComment = findComment(commentId);
        Comment updateComment = findComment;
        updateComment.setContent(comment.getContent());
        updateComment.setUpdatedTime(LocalDateTime.now());

        return updateComment;
    }

    @Transactional
    public void deleteComment(Long commentId){
        Comment comment = findComment(commentId);
        comment.deleteComment();
    }
}
