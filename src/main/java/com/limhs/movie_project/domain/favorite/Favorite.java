package com.limhs.movie_project.domain.favorite;

import com.limhs.movie_project.domain.mappedSuperClass.BaseEntity;
import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter @Setter
public class Favorite extends BaseEntity {

    public Favorite() {
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
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
        movie.getFavorites().remove(this);
        user.getFavorites().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Favorite favorite)) return false;
        return Objects.equals(this.getMovie().getId(), favorite.getMovie().getId()) &&
               Objects.equals(this.getUser().getId(), favorite.getUser().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, user);
    }
}
