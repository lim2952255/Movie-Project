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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final MovieRepository movieRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final MovieService movieService;

    @Value("${tmdb.image.base.url}")
    private String tmdbImageBaseUrl;

    @Transactional
    public void setFavorites(int movieId,HttpSession session){
        Optional<Movie> findMovie = movieRepository.findByMovieId(movieId);
        if(findMovie.isEmpty()){
            throw new RuntimeException("해당 영화가 존재하지 않습니다.");
        }
        Movie movie = findMovie.get();

        User user = userService.getUser(session);

        Optional<Favorite> existFavorite = favoriteRepository.findByUserAndMovie(user, movie); // 중복 체크
        if(existFavorite.isPresent()){
            return;
        }

        Favorite favorite = new Favorite();
        favorite.setFavorite(movie, user);
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void unsetFavorites(int movieId, HttpSession session){
        Movie movie = movieService.findByMovieId(movieId);
        User user = userService.getUser(session);

        Favorite favorite = findFavorite(user, movie);
        favorite.unsetFavorite();

    }

    @Transactional
    public Favorite findFavorite(User user, Movie movie){
        Optional<Favorite> findFavorite = favoriteRepository.findByUserAndMovie(user, movie);
        if(findFavorite.isEmpty()){
            throw new RuntimeException("해당 엔티티를 찾을 수 없습니다");
        }
        return findFavorite.get();
    }

    @Transactional
    public Page<MovieCardDTO> getFavoriteMovie(HttpSession session, int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber,20);

        User user = userService.getUser(session);

        Page<Favorite> findFavorites = favoriteRepository.findByUserId(user.getId(), pageable);
        Page<MovieCardDTO> movieCards = findFavorites.map(favorite -> new MovieCardDTO(favorite.getMovie(), tmdbImageBaseUrl));
        return movieCards;
    }

    @Transactional
    public boolean isFavorite(HttpSession session, Movie movie){
        User user = userService.getUser(session);
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
