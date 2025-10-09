package com.limhs.movie_project.service;

import com.limhs.movie_project.domain.post.Post;
import com.limhs.movie_project.domain.post.PostDTO;
import com.limhs.movie_project.domain.User;
import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.repository.movie.MovieRepository;
import com.limhs.movie_project.repository.post.PostRepository;
import com.limhs.movie_project.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final PostRepository postRepository;
    private Pageable pageable;

    @Transactional
    public Post savePost(Post post, String id, HttpServletRequest request, HttpServletResponse response){
        int movieId = Integer.parseInt(id);
        Movie movie = movieRepository.findByMovieId(movieId).get();

        HttpSession session = request.getSession();
        Object getUser = session.getAttribute("user");

        if(getUser != null){
            User findUser = (User) getUser;
            User user = userRepository.findByUserId(findUser.getUserId()).get();

            post.setPost(movie,user);
            post.setCreatedTime(LocalDateTime.now());

            postRepository.save(post);
            return post;
        }
        throw new RuntimeException();
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
        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            throw new RuntimeException();
        }
        Post post = findPost.get();

        post.deletePost();
        postRepository.delete(post);
    }

    @Transactional
    public Page<PostDTO> findPosts(int pageNumber, Movie movie) {
        //Pageable
        pageable = PageRequest.of(pageNumber, 10);

        Page<Post> posts = postRepository.findByMovie_MovieId(movie.getMovieId(), pageable);

        return posts.map(post -> new PostDTO(post));
    }

    @Transactional
    public Page<PostDTO> getWritePosts(HttpServletRequest request, HttpServletResponse response, int pageNumber) {
        //Pageable
        pageable = PageRequest.of(pageNumber, 10);

        HttpSession session = request.getSession();
        Object getUser = session.getAttribute("user");

        if(getUser != null){
            User findUser = (User) getUser;
            User user = userRepository.findByUserId(findUser.getUserId()).get();

            Page<Post> getWritePosts = postRepository.findByUser_UserId(user.getUserId(), pageable);
            Page<PostDTO> posts = getWritePosts.map(post -> new PostDTO(post));
            return posts;
        }
        throw new RuntimeException();
    }
}
