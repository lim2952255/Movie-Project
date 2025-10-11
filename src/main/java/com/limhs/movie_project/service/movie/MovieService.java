package com.limhs.movie_project.service.movie;

import com.limhs.movie_project.domain.movie.*;
import com.limhs.movie_project.repository.movie.GenreRepository;
import com.limhs.movie_project.repository.movie.MovieGenresRepository;
import com.limhs.movie_project.repository.movie.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieGenresRepository movieGenresRepository;
    private Pageable pageable;

    @Value("${tmdb.image.base.url}")
    private String tmdbImageBaseUrl;

    @Autowired
    public MovieService(MovieRepository movieRepository, GenreRepository genreRepository, MovieGenresRepository movieGenresRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.movieGenresRepository = movieGenresRepository;
    }

    public Movie findByMovieId(int movieId){
        Optional<Movie> findMovie = movieRepository.findByMovieId(movieId);
        if(findMovie.isEmpty()){
            throw new RuntimeException("해당 영화를 찾을 수 없습니다");
        }

        return findMovie.get();
    }

    public Page<MovieCardDTO> findPoplar(int pageNumber){
        //Pageable
        pageable = PageRequest.of(pageNumber, 20);

        Page<Movie> popularMovies = movieRepository.findByIsPopular(true, pageable);

        Page<MovieCardDTO> movieCards = popularMovies.map(movie -> new MovieCardDTO(movie,tmdbImageBaseUrl));
        return movieCards;
    }

    public Page<MovieCardDTO> findPlaying(int pageNumber){
        //Pageable
        pageable = PageRequest.of(pageNumber, 20);

        Page<Movie> playingMovies = movieRepository.findByIsPlaying(true, pageable);

        Page<MovieCardDTO> movieCards = playingMovies.map(movie -> new MovieCardDTO(movie,tmdbImageBaseUrl));

        return movieCards;
    }

    public Page<MovieCardDTO> findOther(int pageNumber){
        //Pageable
        pageable = PageRequest.of(pageNumber, 20);

        Page<Movie> otherMovies = movieRepository.findByIsPopularAndIsPlaying(false, false, pageable);

        Page<MovieCardDTO> movieCards = otherMovies.map(movie -> new MovieCardDTO(movie,tmdbImageBaseUrl));

        return movieCards;
    }

    public MovieCardDTO mappingMovieToMovieCard(Movie movie){
        MovieCardDTO movieCardDTO = new MovieCardDTO(movie, tmdbImageBaseUrl);
        return movieCardDTO;
    }

    public MovieDetailDTO mappingMovieToMovieDetail(Movie movie){
        MovieDetailDTO movieDetailDTO = new MovieDetailDTO(movie, tmdbImageBaseUrl);
        return movieDetailDTO;
    }

    public void getGenresFromMovieGenres(MovieDetailDTO movieDetailDTO){
        List<MovieGenres> movieGenres = movieDetailDTO.getMovieGenres();
        for (MovieGenres movieGenre : movieGenres) {
            movieDetailDTO.getGenres().add(movieGenre.getGenre());
        }
    }

}
