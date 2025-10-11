package com.limhs.movie_project.repository.movie;

import com.limhs.movie_project.domain.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {

    Optional<Genre> findByGenreId(int genreId);
}
