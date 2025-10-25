package com.limhs.movie_project.repository.movie;

import com.limhs.movie_project.domain.movie.Movie;
import jakarta.persistence.QueryHint;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly",value = "true"),
            @QueryHint(name = "org.hibernate.cacheable", value = "true")
            })
    @Query("select m from Movie m where m.movieId = :movieId")
    Optional<Movie> findByMovieIdForRead(@Param("movieId") int movieId);

    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly",value= "true"),
            @QueryHint(name = "org.hibernate.cacheable", value = "true")
            })
    @Query("select m from Movie m where m.movieId = :movieId")
    Optional<Movie> findByMovieIdForUpdate(@Param("movieId") int movieId);

    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly",value = "true"),
            @QueryHint(name = "org.hibernate.cacheable", value = "true")
            })
    Page<Movie> findByIsPopular(boolean isPopular, Pageable pageable);

    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly",value = "true"),
            @QueryHint(name = "org.hibernate.cacheable", value = "true")
            })
    Page<Movie> findByIsPlaying(boolean isPlaying, Pageable pageable);

    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly",value = "true"),
            @QueryHint(name = "org.hibernate.cacheable", value = "true")
            })
    Page<Movie> findByIsPopularAndIsPlaying(boolean isPopular,boolean isPlaying, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Movie m set m.isPopular = false, m.isPlaying = false")
    void resetAllFlags();

    @QueryHints(@QueryHint(name = "org.hibernate.readOnly",value= "true"))
    @Query("select m from Movie m join fetch m.movieGenres where m.movieId = :movieId")
    Optional<Movie> findByIdWithMovieGenres(@Param("movieId") int movieId);
}
