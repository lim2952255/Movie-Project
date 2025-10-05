package com.limhs.movie_project.controller;

import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.domain.movie.MovieCardDTO;
import com.limhs.movie_project.domain.movie.MovieDetailDTO;
import com.limhs.movie_project.service.MovieService;
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

    @GetMapping("/popularList")
    public String redirectPopularList(){
        return "redirect:/movie/popularList/1";
    }

    @GetMapping("/playingList")
    public String redirectPlayingList(){
        return "redirect:/movie/playingList/1";
    }

    @GetMapping("/popularList/{pageNumber}")
    public String popularList(@PathVariable String pageNumber, Model model){
        int number = Integer.parseInt(pageNumber) - 1;
        Page<MovieCardDTO> poplarMovie = movieService.findPoplar(number);

        List<MovieCardDTO> movies = poplarMovie.getContent();

        model.addAttribute("movies", movies);
        model.addAttribute("totalPages", poplarMovie.getTotalPages());
        return "/movie/popularList";
    }

    @GetMapping("/playingList/{pageNumber}")
    public String playingList(@PathVariable String pageNumber, Model model) {
        int number = Integer.parseInt(pageNumber) - 1   ;
        Page<MovieCardDTO> playingMovie = movieService.findPlaying(number);

        List<MovieCardDTO> movies = playingMovie.getContent();

        model.addAttribute("movies", movies);
        model.addAttribute("totalPages", playingMovie.getTotalPages());
        return "/movie/playingList";
    }

    @GetMapping("/{movieId}")
    public String movieDetail(@PathVariable String movieId, Model model){
        int movieNum = Integer.parseInt(movieId);
        Movie movie = movieService.findByMovieId(movieNum);

        MovieDetailDTO movieDetailDTO = movieService.mappingMovieToMovieDetail(movie);
        movieService.getGenresFromMovieGenres(movieDetailDTO);

        model.addAttribute("movie",movieDetailDTO);

        return "movie/movieDetail";
    }
}
