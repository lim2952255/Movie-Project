package com.limhs.movie_project.controller.user;

import com.limhs.movie_project.domain.movie.MovieCardDTO;
import com.limhs.movie_project.domain.post.Post;
import com.limhs.movie_project.service.favorite.FavoriteService;
import com.limhs.movie_project.service.movie.MovieService;
import com.limhs.movie_project.service.post.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final FavoriteService favoriteService;
    private final PostService postService;
    private final MovieService movieService;

    @GetMapping("mypage/favorites")
    public String favoritesRedirect(){
        return "redirect:/mypage/favorites/1";
    }

    @GetMapping("mypage/posts")
    public String postsRedirect(){

        return "redirect:/mypage/posts/1";
    }

    @GetMapping("mypage/likes")
    public String likesRedirect(){

        return "redirect:/mypage/likes/1";
    }

    @GetMapping("mypage/favorites/{pageNumber}")
    public String favorites(@PathVariable String pageNumber,HttpSession session, Model model){
        int number = Integer.parseInt(pageNumber) - 1   ;

        Page<MovieCardDTO> favoriteMovie = favoriteService.getFavoriteMovie(session, number);
        List<MovieCardDTO> movies = favoriteMovie.getContent();

        model.addAttribute("movies", movies);
        model.addAttribute("totalElements", movieService.getTotalElements(favoriteMovie));
        model.addAttribute("currentElements",movieService.getCurrentElements(favoriteMovie));
        model.addAttribute("totalPages", favoriteMovie.getTotalPages());
        model.addAttribute("currentPage", number + 1);

        return "home/favoritelist.html";
    }

    @PostMapping("user/{movieId}/favorite")
    public String favorite(@PathVariable String movieId, HttpSession session){
        int movieNum = Integer.parseInt(movieId);

        favoriteService.setFavorites(movieNum,session);

        return "redirect:/movie/"+movieId;
    }

    @PostMapping("user/{movieId}/unfavorite")
    public String unfavorite(@PathVariable String movieId, HttpSession session){
        int movieNum = Integer.parseInt(movieId);

        favoriteService.unsetFavorites(movieNum,session);

        return "redirect:/movie/"+movieId;
    }

    @GetMapping("mypage/posts/{pageNumber}")
    public String posts(@PathVariable String pageNumber, HttpSession session, Model model){
        int number = Integer.parseInt(pageNumber) - 1   ;

        Page<Post> getPosts = postService.getWritePosts(session, number);
        List<Post> posts = getPosts.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("totalElements", postService.getTotalElements(getPosts));
        model.addAttribute("currentElements",postService.getCurrentElements(getPosts));
        model.addAttribute("totalPages", getPosts.getTotalPages());
        model.addAttribute("currentPage", number + 1);

        return "home/postlist.html";
    }

    @GetMapping("mypage/likes/{pageNumber}")
    public String likes(@PathVariable String pageNumber, HttpSession session, Model model){
        int number = Integer.parseInt(pageNumber) - 1   ;

        Page<Post> getPosts = postService.getLikesPosts(session, number);
        List<Post> posts = getPosts.getContent();

        model.addAttribute("posts", posts);
        model.addAttribute("totalElements", postService.getTotalElements(getPosts));
        model.addAttribute("currentElements",postService.getCurrentElements(getPosts));
        model.addAttribute("totalPages", getPosts.getTotalPages());
        model.addAttribute("currentPage", number + 1);
        return "home/likelist.html";
    }
}
