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
public interface MovieRepository extends JpaRepository<Movie, Long>, CustomMovieRepository {
    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly",value = "true"),
            @QueryHint(name = "org.hibernate.cacheable", value = "true")
            })
    @Query("select m from Movie m where m.movieId = :movieId")
    Optional<Movie> findByMovieIdForRead(@Param("movieId") int movieId);

    @Query("select m from Movie m where m.movieId = :movieId")
    Optional<Movie> findByMovieIdForUpdate(@Param("movieId") int movieId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Movie m set m.isPopular = false, m.isPlaying = false")
    void resetAllFlags();

    @QueryHints({@QueryHint(name = "org.hibernate.readOnly",value= "true"),
                @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select m from Movie m join fetch m.movieGenres where m.movieId = :movieId")
    Optional<Movie> findByIdWithMovieGenres(@Param("movieId") int movieId);

    List<Movie> findByMovieIdIn(List<Integer> movieIdList);

    // 스케줄러에서 수정하는 용도의 메서드
    List<Movie> findByIsPlayingTrueOrIsPopularTrue();
}
