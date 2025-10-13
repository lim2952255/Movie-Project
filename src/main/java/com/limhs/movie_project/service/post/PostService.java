package com.limhs.movie_project.service.post;

import com.limhs.movie_project.domain.post.Post;
import com.limhs.movie_project.domain.post.PostDTO;
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
        post.setCreatedTime(LocalDateTime.now());

        postRepository.save(post);
        return post;

    }

    @Transactional
    public Post updatePost(Post updatedPost, Long id){
        Post post = postRepository.findById(id).get();

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());

        return post;
    }

    @Transactional
    public Post findPost(Long postId){
        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            throw new RuntimeException();
        }
        Post post = findPost.get();
        post.setViewCount(post.getViewCount() + 1); //조회수 증가
        return post;
    }

    @Transactional
    public void deletePost(Long postId){
        Post post = findPost(postId);
        post.deletePost();
    }

    @Transactional
    public Page<PostDTO> findPosts(int pageNumber, Movie movie) {
        //Pageable
        pageable = PageRequest.of(pageNumber, 10);

        Page<Post> posts = postRepository.findByMovie_MovieId(movie.getMovieId(), pageable);

        return posts.map(post -> new PostDTO(post));
    }

    @Transactional
    public Page<PostDTO> getWritePosts(HttpSession session, int pageNumber) {
        //Pageable
        pageable = PageRequest.of(pageNumber, 10);

        User user = userService.getUser(session);

        Page<Post> getWritePosts = postRepository.findByUser_UserId(user.getUserId(), pageable);
        Page<PostDTO> posts = getWritePosts.map(post -> new PostDTO(post));

        return posts;

    }

    @Transactional
    public Page<PostDTO> getLikesPosts(HttpSession session, int pageNumber) {
        //Pageable
        pageable = PageRequest.of(pageNumber, 10);

        User user = userService.getUser(session);

        Page<Post> getlikePosts = postRepository.findByLikes_User(user, pageable);
        Page<PostDTO> posts = getlikePosts.map(post -> new PostDTO(post));

        return posts;
    }
}
