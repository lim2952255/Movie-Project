package com.limhs.movie_project.repository;

import com.limhs.movie_project.domain.movie.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {

    Optional<Genre> findByGenreId(int genreId);
}
