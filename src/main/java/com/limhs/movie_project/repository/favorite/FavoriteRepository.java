package com.limhs.movie_project.repository.favorite;

import com.limhs.movie_project.domain.favorite.Favorite;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.movie.Movie;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    @QueryHints(@QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Page<Favorite> findByUserId(Long UserId, Pageable pageable);

    @QueryHints(@QueryHint(name = "org.hibernate.readOnly", value = "true"))
    @Query("select f from Favorite f where f.user = :user AND f.movie = :movie")
    Optional<Favorite> findByUserAndMovieForRead(@Param("user") User user,@Param("movie") Movie movie);

    @Query("select f from Favorite f where f.user = :user AND f.movie = :movie")
    Optional<Favorite> findByUserAndMovieForUpdate(@Param("user") User user,@Param("movie") Movie movie);
}