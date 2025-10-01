package com.limhs.movie_project.service;

import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.domain.movie.MovieCard;
import com.limhs.movie_project.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private Pageable pageable;

    @Value("${tmdb.image.base.url}")
    private String tmdbImageBaseUrl;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Page<MovieCard> findPoplar(int pageNumber){
        //Pageable
        pageable = PageRequest.of(pageNumber, 18);

        Page<Movie> popularMovies = movieRepository.findByIsPopular(true, pageable);

        Page<MovieCard> movieCards = popularMovies.map(movie -> new MovieCard(movie,tmdbImageBaseUrl));
        return movieCards;
    }

    public Page<MovieCard> findPlaying(int pageNumber){
        //Pageable
        pageable = PageRequest.of(pageNumber, 18);

        Page<Movie> playingMovies = movieRepository.findByIsPlaying(true, pageable);

        Page<MovieCard> movieCards = playingMovies.map(movie -> new MovieCard(movie,tmdbImageBaseUrl));

        return movieCards;
    }
}
