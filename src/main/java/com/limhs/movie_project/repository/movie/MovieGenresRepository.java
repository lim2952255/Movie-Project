package com.limhs.movie_project.repository.movie;

import com.limhs.movie_project.domain.movie.Genre;
import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.domain.movie.MovieGenres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieGenresRepository extends JpaRepository<MovieGenres, Long> {
    Optional<MovieGenres> findByMovieAndGenre(Movie movie, Genre genre);
}
