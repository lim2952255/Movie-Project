package com.limhs.movie_project.service.post;

import com.limhs.movie_project.domain.post.Post;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.repository.post.PostRepository;
import com.limhs.movie_project.service.movie.MovieService;
import com.limhs.movie_project.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final MovieService movieService;
    private Pageable pageable;

    @Transactional
    public Post savePost(Post post, String id, HttpSession session){
        int movieId = Integer.parseInt(id);

        Movie movie = movieService.findByMovieId(movieId);
        User user = userService.getUser(session);

        post.setPost(movie,user);

        postRepository.save(post);
        return post;

    }

    @Transactional
    public Post updatePost(Post updatedPost, Long id){
        Post post = postRepository.findById(id).get();

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setUpdatedTime(LocalDateTime.now());

        return post;
    }

    @Transactional
    public Post findPost(Long postId){
        try{
            Optional<Post> findPost = postRepository.findByIdWithLock(postId);

            if(findPost.isEmpty()){
                throw new RuntimeException();
            }
            Post post = findPost.get();

            post.setViewCount(post.getViewCount() + 1);
            return post;
        } catch (Exception e){
            throw new RuntimeException("락 타임아웃", e);
        }
    }

    @Transactional
    public void deletePost(Long postId){
        Post post = findPost(postId);
        post.deletePost();
    }

    @Transactional
    public Page<Post> findPosts(int pageNumber, Movie movie) {
        //Pageable
        pageable = PageRequest.of(pageNumber, 10);

        Page<Post> posts = postRepository.findByMovie_MovieId(movie.getMovieId(), pageable);

        return posts;
    }

    @Transactional
    public Page<Post> getWritePosts(HttpSession session, int pageNumber) {
        //Pageable
        pageable = PageRequest.of(pageNumber, 10);

        User user = userService.getUser(session);

        Page<Post> posts = postRepository.findByUser_UserId(user.getUserId(), pageable);

        return posts;

    }

    @Transactional
    public Page<Post> getLikesPosts(HttpSession session, int pageNumber) {
        //Pageable
        pageable = PageRequest.of(pageNumber, 10);

        User user = userService.getUser(session);

        Page<Post> posts = postRepository.findByLikes_User(user, pageable);

        return posts;
    }
}
