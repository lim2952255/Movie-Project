package com.limhs.movie_project.repository.movie;

import com.limhs.movie_project.domain.movie.Movie;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByMovieId(int movieId);

    Page<Movie> findByIsPopular(boolean isPopular, Pageable pageable);

    Page<Movie> findByIsPlaying(boolean isPlaying, Pageable pageable);

    Page<Movie> findByIsPopularAndIsPlaying(boolean isPopular,boolean isPlaying, Pageable pageable);

    Optional<List<Integer>> findGenreIdsByMovieId(int movieId);

    @Modifying
    @Transactional
    @Query("UPDATE Movie m set m.isPopular = false, m.isPlaying = false")
    void resetAllFlags();

//    @Modifying
//    @Transactional
//    @Query("DELETE FROM Movie m WHERE m.isPopular = false AND m.isPlaying = false")
//    void deleteAllUnused();


    List<Movie> findAllByIsPlayingFalseAndIsPopularFalse();
}
