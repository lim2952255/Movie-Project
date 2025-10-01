package com.limhs.movie_project.domain.movie;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter @Setter
public class MovieCard {

    public MovieCard(Movie movie, String tmdbImageBaseUrl) {
        movieId = movie.getMovieId();
        posterPath = tmdbImageBaseUrl + movie.getPosterPath();
        title = movie.getTitle();
    }

    private int movieId;

    private String posterPath;

    private String title;
}
