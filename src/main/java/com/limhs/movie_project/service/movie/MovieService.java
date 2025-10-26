package com.limhs.movie_project.service.movie;

import com.limhs.movie_project.domain.genre.Genre;
import com.limhs.movie_project.domain.movie.*;
import com.limhs.movie_project.repository.movie.GenreRepository;
import com.limhs.movie_project.repository.movie.MovieGenresRepository;
import com.limhs.movie_project.repository.movie.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private Pageable pageable;

    @Autowired
    public MovieService(MovieRepository movieRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;

    }

    @Transactional(readOnly = true)
    public Movie findByMovieIdForRead(int movieId){
        return movieRepository.findByMovieIdForRead(movieId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Movie findByMovieIdWithMoviegenres(int movieId){
        return movieRepository.findByIdWithMovieGenres(movieId).orElse(null);
    }

    @Transactional
    public Movie findByMovieIdForUpdate(int movieId){
        return movieRepository.findByMovieIdForUpdate(movieId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findPopular(int pageNumber, String sortParam){
        Sort sort = findMovieSort(sortParam);

        if(sort == null){
            //Pageable
            pageable = PageRequest.of(pageNumber, 20);
        } else{
            pageable = PageRequest.of(pageNumber, 20 ,sort);
        }

        Page<Movie> popularMovies = movieRepository.findByIsPopular(true, pageable);

        Page<MovieCardDTO> movieCards = popularMovies.map(movie -> new MovieCardDTO(movie));
        return movieCards;
    }

    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findPlaying(int pageNumber, String sortParam){
        Sort sort = findMovieSort(sortParam);

        if(sort == null){
            //Pageable
            pageable = PageRequest.of(pageNumber, 20);
        } else{
            pageable = PageRequest.of(pageNumber, 20 ,sort);
        }

        Page<Movie> playingMovies = movieRepository.findByIsPlaying(true, pageable);

        Page<MovieCardDTO> movieCards = playingMovies.map(movie -> new MovieCardDTO(movie));

        return movieCards;
    }

    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findOther(int pageNumber, String sortParam){
        Sort sort = findMovieSort(sortParam);

        if(sort == null){
            //Pageable
            pageable = PageRequest.of(pageNumber, 20);
        } else{
            pageable = PageRequest.of(pageNumber, 20 ,sort);
        }

        Page<Movie> otherMovies = movieRepository.findByIsPopularAndIsPlaying(false, false, pageable);

        Page<MovieCardDTO> movieCards = otherMovies.map(movie -> new MovieCardDTO(movie));

        return movieCards;
    }

    @Transactional(readOnly = true)
    public List<Genre> getGenresFromMovie(Movie movie){
        List<MovieGenres> movieGenres = movie.getMovieGenres();

        List<Long> movieGenresIds = movieGenres.stream().map(movieGenre -> movieGenre.getId()).toList();
        List<Genre> genres = genreRepository.findByMovieGenresIdIn(movieGenresIds);
        return genres;
    }

    @Transactional(readOnly = true)
    public long getTotalElements(Page<MovieCardDTO> movieCardList){
        return movieCardList.getTotalElements();
    }

    @Transactional(readOnly = true)
    public int getCurrentElements(Page<MovieCardDTO> movieCardList){
        return movieCardList.getNumber() * movieCardList.getSize() + movieCardList.getNumberOfElements();
    }

    @Transactional(readOnly = true)
    public Sort findMovieSort(String sort){
        switch (sort) {
            case "popularity":
                return Sort.by(Sort.Direction.DESC, "popularity");
            case "releaseDate":
                return Sort.by(Sort.Direction.DESC, "releaseDate");
            default:
                return null;
        }
    }
}
