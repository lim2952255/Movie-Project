package com.limhs.movie_project.repository;

import com.limhs.movie_project.domain.Favorite;
import com.limhs.movie_project.domain.User;
import com.limhs.movie_project.domain.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    Page<Favorite> findByUserId(Long UserId, Pageable pageable);

    Optional<Favorite> findByUserAndMovie(User user, Movie movie);

}
