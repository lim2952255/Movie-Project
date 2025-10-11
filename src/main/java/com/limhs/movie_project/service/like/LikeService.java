package com.limhs.movie_project.service.like;

import com.limhs.movie_project.domain.like.Like;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.post.Post;
import com.limhs.movie_project.repository.like.LikeRepository;
import com.limhs.movie_project.repository.post.PostRepository;
import com.limhs.movie_project.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserService userService;

    @Transactional
    public void saveLike(Long postId, HttpServletRequest request, HttpServletResponse response){
        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            throw new RuntimeException();
        }
        Post post = findPost.get();

        User user = userService.getUser(request, response);

        Like like = new Like();

        like.setLike(post, user);
        likeRepository.save(like);
    }

    @Transactional
    public void deleteLike(Long postId, HttpServletRequest request, HttpServletResponse response){
        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            throw new RuntimeException();
        }
        Post post = findPost.get();
        User user = userService.getUser(request, response);

        Optional<Like> findLike = likeRepository.findByPostAndUser(post, user);
        if(findLike.isEmpty()){
            throw new RuntimeException();
        }
        Like like = findLike.get();

        like.deleteLike();
        likeRepository.delete(like);
    }

    public boolean userLikesPost(Post post, User user){
        Optional<Like> findLike = likeRepository.findByPostAndUser(post, user);
        if(findLike.isEmpty()){
            return false;
        }
        return true;
    }

    public void deleteByPost(Post post){
        likeRepository.deleteByPost(post);
    }
}
