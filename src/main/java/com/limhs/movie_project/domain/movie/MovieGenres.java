package com.limhs.movie_project.domain.movie;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class MovieGenres {
    public MovieGenres() {
    }

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public void setMovieGenres(Movie movie, Genre genre){
        if(this.movie != null && this.movie != movie){
            this.movie.getMovieGenres().remove(this);
        }

        this.movie = movie;

        if(this.movie != null && !movie.getMovieGenres().contains(this)){
            movie.getMovieGenres().add(this);
        }

        this.genre = genre;
    }
}
