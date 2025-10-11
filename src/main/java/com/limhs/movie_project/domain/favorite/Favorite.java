package com.limhs.movie_project.domain.favorite;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.movie.Movie;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Favorite {

    public Favorite() {
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setFavorite(Movie movie, User user){
        if(this.movie != null && this.movie != movie){
            this.movie.getFavorites().remove(this);
        }

        this.movie = movie;

        if(movie != null && !movie.getFavorites().contains(this)){
            movie.getFavorites().add(this);
        }

        if(this.user != null && this.user != user){
            this.user.getFavorites().remove(this);
        }

        this.user = user;

        if(user != null && !user.getFavorites().contains(this)){
            user.getFavorites().add(this);
        }
    }

    public void unsetFavorite(){
        if(movie != null && movie.getFavorites().contains(this)){
            movie.getFavorites().remove(this);
        }
        this.movie = null;

        if(user != null && user.getFavorites().contains(this)){
            user.getFavorites().remove(this);
        }
        this.user = null;
    }
}
