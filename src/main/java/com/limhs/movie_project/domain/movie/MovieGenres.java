package com.limhs.movie_project.domain.movie;

import com.limhs.movie_project.domain.genre.Genre;
import com.limhs.movie_project.domain.mappedSuperClass.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY;

@Entity
@Getter @Setter
@Cache(usage = READ_ONLY)
public class MovieGenres extends BaseEntity {
    public MovieGenres() {
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    public void setMovieGenres(Movie movie, Genre genre){
        if(this.movie != null && this.movie != movie){
            this.movie.getMovieGenres().remove(this);
        }

        this.movie = movie;

        if(this.movie != null && !movie.getMovieGenres().contains(this)){
            this.movie.getMovieGenres().add(this);
        }

        if(this.genre != null && this.genre != genre){
            this.genre.getMovieGenres().remove(this);
        }

        this.genre = genre;

        if(this.genre != null && !genre.getMovieGenres().contains(this)){
            this.genre.getMovieGenres().add(this);
        }
    }
}
