package com.limhs.movie_project.service;

import com.limhs.movie_project.domain.Favorite;
import com.limhs.movie_project.domain.LoginDTO;
import com.limhs.movie_project.domain.User;
import com.limhs.movie_project.domain.movie.Movie;
import com.limhs.movie_project.domain.movie.MovieCardDTO;
import com.limhs.movie_project.exception.DuplicatedUserId;
import com.limhs.movie_project.exception.LoginFailException;
import com.limhs.movie_project.repository.FavoriteRepository;
import com.limhs.movie_project.repository.MovieRepository;
import com.limhs.movie_project.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final FavoriteRepository favoriteRepository;
    private final MovieService movieService;
    private final PasswordEncoder passwordEncoder;

    public User save(User user) throws DuplicatedUserId {
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        Optional<User> findUser = userRepository.findByUserId(user.getUserId());

        if(findUser.isPresent()){
            throw new DuplicatedUserId("사용자 id가 중복됩니다. 다른 id로 시도해주세요");
        }

        userRepository.save(user);
        return user;
    }

    public User login(LoginDTO loginDTO) throws LoginFailException {
        Optional<User> findUser = userRepository.findByUserId(loginDTO.getUserId());

        if(findUser.isEmpty()){
            throw new LoginFailException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        boolean matches = passwordEncoder.matches(loginDTO.getPassword(), findUser.get().getPassword());

        if(!matches){
            throw new LoginFailException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return findUser.get();
    }

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

    public List<MovieCardDTO> getFavoriteMovie(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);

        if(session != null){
            Object findUser = session.getAttribute("user");
            if(findUser != null){
                User user = (User) findUser;
                List<Favorite> findFavorites = favoriteRepository.findByUserId(user.getId());
                List<MovieCardDTO> movies = new ArrayList<>();
                for (Favorite findFavorite : findFavorites) {
                    Movie movie = findFavorite.getMovie();
                    MovieCardDTO movieCardDTO = movieService.mappingMovieToMovieCard(movie);
                    movies.add(movieCardDTO);
                }
                return movies;

            }
        }
        throw new RuntimeException();
    }

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
