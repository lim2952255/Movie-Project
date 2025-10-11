package com.limhs.movie_project.controller.movie;

import com.limhs.movie_project.domain.post.PostDTO;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.domain.movie.MovieCardDTO;
import com.limhs.movie_project.domain.movie.MovieDetailDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final FavoriteService favoriteService;
    private final PostService postService;

    @GetMapping("/popularList")
    public String redirectPopularList(){
        return "redirect:/movie/popularList/1";
    }

    @GetMapping("/playingList")
    public String redirectPlayingList(){
        return "redirect:/movie/playingList/1";
    }

    @GetMapping("/otherList")
    public String redirectOtherList(){
        return "redirect:/movie/otherList/1";
    }

    @GetMapping("/{movieId}")
    public String redirectMovieDetail(@PathVariable String movieId){
        return "redirect:/movie/"+movieId+"/1";
    }

    @GetMapping("/popularList/{pageNumber}")
    public String popularList(@PathVariable String pageNumber, Model model){
        int number = Integer.parseInt(pageNumber) - 1;
        Page<MovieCardDTO> poplarMovie = movieService.findPoplar(number);

        List<MovieCardDTO> movies = poplarMovie.getContent();

        model.addAttribute("movies", movies);
        model.addAttribute("totalPages", poplarMovie.getTotalPages());
        model.addAttribute("currentPage", number + 1);
        return "/movie/popularList";
    }

    @GetMapping("/playingList/{pageNumber}")
    public String playingList(@PathVariable String pageNumber, Model model) {
        int number = Integer.parseInt(pageNumber) - 1   ;
        Page<MovieCardDTO> playingMovie = movieService.findPlaying(number);

        List<MovieCardDTO> movies = playingMovie.getContent();

        model.addAttribute("movies", movies);
        model.addAttribute("totalPages", playingMovie.getTotalPages());
        model.addAttribute("currentPage", number + 1);
        return "/movie/playingList";
    }

    @GetMapping("/otherList/{pageNumber}")
    public String otherList(@PathVariable String pageNumber, Model model) {
        int number = Integer.parseInt(pageNumber) - 1   ;
        Page<MovieCardDTO> otherMovie = movieService.findOther(number);

        List<MovieCardDTO> movies = otherMovie.getContent();

        model.addAttribute("movies", movies);
        model.addAttribute("totalPages", otherMovie.getTotalPages());
        model.addAttribute("currentPage", number + 1);
        return "/movie/otherList";
    }

    @GetMapping("/{movieId}/{pageNumber}")
    public String movieDetail(@PathVariable String movieId, @PathVariable String pageNumber,HttpSession httpSession, Model model){
        int movieNum = Integer.parseInt(movieId);
        Movie movie = movieService.findByMovieId(movieNum);

        MovieDetailDTO movieDetailDTO = movieService.mappingMovieToMovieDetail(movie);
        movieService.getGenresFromMovieGenres(movieDetailDTO);

        model.addAttribute("movie",movieDetailDTO);

        Object findUser = httpSession.getAttribute("user");
        User getUser = (User) findUser;

        boolean favorite = favoriteService.isFavorite(getUser, movie);
        model.addAttribute("isFavorite",favorite);

        int number = Integer.parseInt(pageNumber) - 1   ;
        Page<PostDTO> post = postService.findPosts(number, movie);

        List<PostDTO> posts = post.getContent();
        model.addAttribute("posts",posts);
        model.addAttribute("totalPages", post.getTotalPages());
        model.addAttribute("currentPage", number + 1);
        return "movie/movieDetail";
    }

}
