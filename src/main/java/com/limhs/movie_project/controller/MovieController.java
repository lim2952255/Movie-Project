package com.limhs.movie_project.controller;

import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.domain.movie.MovieCard;
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

    @GetMapping("/popularList/{pageNumber}")
    public String popularList(@PathVariable String pageNumber, Model model){
        int number = Integer.parseInt(pageNumber) - 1;
        Page<MovieCard> poplarMovie = movieService.findPoplar(number);

        List<MovieCard> movies = poplarMovie.getContent();

        model.addAttribute("movies", movies);
        model.addAttribute("totalPages", poplarMovie.getTotalPages());
        return "/movie/popularList";
    }

    @GetMapping("/playingList/{pageNumber}")
    public String playingList(@PathVariable String pageNumber, Model model) {
        int number = Integer.parseInt(pageNumber) - 1   ;
        Page<MovieCard> playingMovie = movieService.findPlaying(number);

        List<MovieCard> movies = playingMovie.getContent();

        model.addAttribute("movies", movies);
        model.addAttribute("totalPages", playingMovie.getTotalPages());
        return "/movie/playingList";

    }
}
