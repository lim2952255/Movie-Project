package com.limhs.movie_project.repository.movie;

import com.limhs.movie_project.domain.movie.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomMovieRepository {
    Page<Movie> findMoviePages(Pageable pageable, String sortParam ,String movieName, boolean isPopular, boolean isPlaying);
}
