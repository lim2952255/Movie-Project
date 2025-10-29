package com.limhs.movie_project.service.favorite;

import com.limhs.movie_project.domain.favorite.Favorite;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.domain.movie.MovieCardDTO;
import com.limhs.movie_project.repository.favorite.FavoriteRepository;
import com.limhs.movie_project.repository.movie.MovieRepository;
import com.limhs.movie_project.service.movie.MovieService;
import com.limhs.movie_project.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final MovieRepository movieRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final MovieService movieService;

    @Transactional
    public void setFavorites(int movieId){
        Optional<Movie> findMovie = movieRepository.findByMovieIdForUpdate(movieId);
        if(findMovie.isEmpty()){
            throw new RuntimeException("해당 영화가 존재하지 않습니다.");
        }
        Movie movie = findMovie.get();

        User user = userService.getUserForUpdate();

        Favorite existFavorite = findFavoriteForRead(user,movie);
        if(existFavorite != null){
            return;
        }

        Favorite favorite = new Favorite();
        favorite.setFavorite(movie, user);
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void unsetFavorites(int movieId){
        Movie movie = movieService.findByMovieIdForUpdate(movieId);
        User user = userService.getUserForUpdate();

        Favorite favorite = findFavoriteForUpdate(user, movie);
        favorite.unsetFavorite();

    }

    @Transactional
    public Favorite findFavoriteForRead(User user, Movie movie){
        return favoriteRepository.findByUserAndMovieForRead(user,movie).orElse(null);
    }

    @Transactional
    public Favorite findFavoriteForUpdate(User user, Movie movie){
        return favoriteRepository.findByUserAndMovieForUpdate(user,movie).orElse(null);
    }

    @Transactional(readOnly = true)
    public Page<MovieCardDTO> getFavoriteMovie(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber, 20);

        User user = userService.getUserForRead();

        Page<Favorite> findFavorites = favoriteRepository.findByUserId(user.getId(), pageable);
        Page<MovieCardDTO> movieCards = findFavorites.map(favorite -> new MovieCardDTO(favorite.getMovie()));
        return movieCards;
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(Movie movie){
        User user = userService.getUserForRead();
        List<Favorite> favorites = user.getFavorites();

        boolean isFavorites = false;

        for (Favorite favorite : favorites) {
            if(favorite.getMovie() == movie){
                isFavorites = true;
                break;
            }
        }
        return isFavorites;
    }
}
