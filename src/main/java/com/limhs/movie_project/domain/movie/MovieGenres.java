package com.limhs.movie_project.domain.movie;

import com.limhs.movie_project.domain.genre.Genre;
import com.limhs.movie_project.domain.mappedSuperClass.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class MovieGenres extends BaseEntity {
    public MovieGenres() {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
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
