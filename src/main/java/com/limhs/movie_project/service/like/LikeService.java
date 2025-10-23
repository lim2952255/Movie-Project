package com.limhs.movie_project.service.like;

import com.limhs.movie_project.domain.like.Like;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.post.Post;
import com.limhs.movie_project.repository.like.LikeRepository;
import com.limhs.movie_project.service.post.PostService;
import com.limhs.movie_project.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    @Transactional
    public void saveLike(Long postId, HttpSession session){
        Post post = postService.findPostForUpdate(postId);
        User user = userService.getUserForUpdate(session);

        Like like = new Like();

        like.setLike(post, user);
        likeRepository.save(like);
    }

    @Transactional
    public Like findLike(Post post, User user){
        Optional<Like> findLike = likeRepository.findByPostAndUser(post, user);
        if(findLike.isEmpty()){
            throw new RuntimeException();
        }
        return findLike.get();
    }

    @Transactional
    public void deleteLike(Long postId,HttpSession session){
        Post post = postService.findPostForUpdate(postId);
        User user = userService.getUserForUpdate(session);
        Like like = findLike(post, user);
        like.deleteLike();
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
