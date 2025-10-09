package com.limhs.movie_project.service;

import com.limhs.movie_project.domain.Favorite;
import com.limhs.movie_project.domain.User;
import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.domain.movie.MovieCardDTO;
import com.limhs.movie_project.repository.favorite.FavoriteRepository;
import com.limhs.movie_project.repository.movie.MovieRepository;
import com.limhs.movie_project.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    @Value("${tmdb.image.base.url}")
    private String tmdbImageBaseUrl;

    @Transactional
    public void setFavorites(int movieId, HttpServletRequest request, HttpServletResponse response){
        Optional<Movie> findMovie = movieRepository.findByMovieId(movieId);
        if(findMovie.isEmpty()){
            throw new RuntimeException("해당 영화가 존재하지 않습니다.");
        }
        Movie movie = findMovie.get();

        HttpSession session = request.getSession(false);

        if(session != null){
            Object findUser = session.getAttribute("user");
            if(findUser != null){
                User getUser = (User) findUser;
                Optional<User> user = userRepository.findByUserId(getUser.getUserId());

                Optional<Favorite> existFavorite = favoriteRepository.findByUserAndMovie(user.get(), movie); // 중복 체크
                if(existFavorite.isPresent()){
                    return;
                }

                Favorite favorite = new Favorite();
                favorite.setFavorite(movie, user.get());
                favoriteRepository.save(favorite);
            }
        }
    }

    @Transactional
    public void unsetFavorites(int movieId, HttpServletRequest request, HttpServletResponse response){
        Optional<Movie> findMovie = movieRepository.findByMovieId(movieId);
        if(findMovie.isEmpty()){
            throw new RuntimeException("해당 영화가 존재하지 않습니다.");
        }
        Movie movie = findMovie.get();

        HttpSession session = request.getSession(false);

        if(session != null){
            Object findUser = session.getAttribute("user");
            if(findUser != null){
                User getUser = (User) findUser;
                Optional<User> user = userRepository.findByUserId(getUser.getUserId());

                Favorite favorite = favoriteRepository.findByUserAndMovie(user.get(), movie).get();
                favorite.unsetFavorite();
                favoriteRepository.delete(favorite);
            }
        }
    }

    @Transactional
    public Page<MovieCardDTO> getFavoriteMovie(HttpServletRequest request, HttpServletResponse response, int pageNumber){
        HttpSession session = request.getSession(false);
        Pageable pageable = PageRequest.of(pageNumber,20);

        if(session != null){
            Object findUser = session.getAttribute("user");
            if(findUser != null){
                User user = (User) findUser;
                Page<Favorite> findFavorites = favoriteRepository.findByUserId(user.getId(), pageable);
                Page<MovieCardDTO> movieCards = findFavorites.map(favorite -> new MovieCardDTO(favorite.getMovie(), tmdbImageBaseUrl));
                return movieCards;

            }
        }
        throw new RuntimeException();
    }

    @Transactional
    public boolean isFavorite(User sessionUser, Movie movie){
        Optional<User> findUser = userRepository.findByUserId(sessionUser.getUserId());
        User user = findUser.get();

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
