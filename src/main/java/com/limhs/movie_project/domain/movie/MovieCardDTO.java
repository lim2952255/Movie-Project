package com.limhs.movie_project.domain.movie;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MovieCardDTO {

    public MovieCardDTO(Movie movie, String tmdbImageBaseUrl) {
        adult = movie.isAdult();
        isPopular = movie.isPopular();
        isPlaying = movie.isPlaying();
        movieId = movie.getMovieId();
        posterPath = tmdbImageBaseUrl + movie.getPosterPath();
        title = movie.getTitle();
    }

    private boolean adult;

    private boolean isPlaying;

    private boolean isPopular;

    private int movieId;

    private String posterPath;

    private String title;


}
