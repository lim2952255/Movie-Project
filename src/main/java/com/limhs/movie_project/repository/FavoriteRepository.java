package com.limhs.movie_project.repository;

import com.limhs.movie_project.domain.Favorite;
import com.limhs.movie_project.domain.User;
import com.limhs.movie_project.domain.movie.Genre;
import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.domain.movie.MovieGenres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    List<Favorite> findByUserId(Long UserId);

    Optional<Favorite> findByUserAndMovie(User user, Movie movie);

}
