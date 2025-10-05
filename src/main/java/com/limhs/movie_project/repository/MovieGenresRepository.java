package com.limhs.movie_project.repository;

import com.limhs.movie_project.domain.movie.MovieGenres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieGenresRepository extends JpaRepository<MovieGenres, Long> {
}
