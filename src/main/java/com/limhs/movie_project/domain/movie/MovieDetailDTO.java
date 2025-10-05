package com.limhs.movie_project.domain.movie;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MovieDetailDTO {
    public MovieDetailDTO(Movie movie , String tmdbImageBaseUrl) {
        adult = movie.isAdult();
        isPlaying = movie.isPlaying();
        isPopular = movie.isPopular();
        genreIds = movie.getGenreIds();
        movieId = movie.getMovieId();
        overview = movie.getOverview();
        popularity = movie.getPopularity();
        posterPath = tmdbImageBaseUrl + movie.getPosterPath();
        releaseDate = movie.getReleaseDate();
        title = movie.getTitle();
        movieGenres = movie.getMovieGenres();
    }
    private List<MovieGenres> movieGenres;

    private List<Genre> genres = new ArrayList<>();

    private boolean adult = true;

    private boolean isPlaying = false;

    private boolean isPopular = false;

    private List<Integer> genreIds;

    private int movieId;

    private String overview;

    private Double popularity;

    private String posterPath;

    private String releaseDate;

    private String title;

}
