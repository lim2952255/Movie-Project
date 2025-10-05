package com.limhs.movie_project.service.scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limhs.movie_project.domain.movie.*;
import com.limhs.movie_project.repository.GenreRepository;
import com.limhs.movie_project.repository.MovieGenresRepository;
import com.limhs.movie_project.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MovieSchedule {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieGenresRepository movieGenresRepository;

    @Value("${tmdb.api.key}")
    private String tmdbApiKey;

    @Autowired
    public MovieSchedule(MovieRepository movieRepository, GenreRepository genreRepository, MovieGenresRepository movieGenresRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.movieGenresRepository = movieGenresRepository;
    }
    
    //매일 09시에 반복적으로 로직 수행
    @Scheduled(cron = "0 35 19 * * *")
    @Transactional
    public void movieListUpdate() throws IOException, InterruptedException {
        log.info("Genre Update start");
        getGenres();
        log.info("Genre Update end");

        movieRepository.resetAllFlags();

        log.info("Update start");

        getPopularMovie();
        getPlayingMovie();

        log.info("Update end");

        movieRepository.deleteAllUnused();
    }

    @Transactional
    public void getGenres() throws IOException, InterruptedException {
        HttpRequest request;
        HttpResponse<String> response;
        ObjectMapper mapper = new ObjectMapper();

        request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/genre/movie/list?language=ko"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiZTIxNjc4ZDg5YzM2MWRmNDE3ODNmOTQ5ODM3NjQ3NyIsIm5iZiI6MTc1NzQ2NTI5MS45Njg5OTk5LCJzdWIiOiI2OGMwY2FjYmY0ZmM3NDRlNTE5OGM2NmUiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.hMpJWMjbeV_L14WlGEildsyP_PmQPDknj3M8FP7s02o")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        GenreList genreList = mapper.readValue(response.body(), GenreList.class);
        List<Genre> genres = genreList.getGenres();

        for (Genre genre : genres) {
            if(genreRepository.findByGenreId(genre.getGenreId()).isEmpty()){
                genreRepository.save(genre);
            }
        }
    }

    @Transactional
    public void getPopularMovie() throws IOException, InterruptedException {
        HttpRequest request;
        HttpResponse<String> response;
        ObjectMapper mapper = new ObjectMapper();

        for (int i = 1; i <= 5; i++) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.themoviedb.org/3/movie/popular?language=en-US&page=" + i))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + tmdbApiKey)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            MovieList popularMovieList = mapper.readValue(response.body(), MovieList.class);
            List<Movie> results = popularMovieList.getResults();

            for (Movie movie : results) {
                movie.setPopular(true);
                Optional<Movie> findMovie = movieRepository.findByMovieId(movie.getMovieId());
                if(findMovie.isEmpty()){
                    movieRepository.save(movie);
                    mappingGenreWithMovie(movie);
                }
                else{
                    Movie popularMovie = findMovie.get();
                    popularMovie.setPopular(true);
                }
            }
            if(popularMovieList.getTotalPages() == i){
                break;
            }
        }
    }

    @Transactional
    public void getPlayingMovie() throws IOException, InterruptedException {
        HttpRequest request;
        HttpResponse<String> response;
        ObjectMapper mapper = new ObjectMapper();

        for (int i = 1; i <= 5; i++) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.themoviedb.org/3/movie/now_playing?language=ko&page=" + i + "&region=KR"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiZTIxNjc4ZDg5YzM2MWRmNDE3ODNmOTQ5ODM3NjQ3NyIsIm5iZiI6MTc1NzQ2NTI5MS45Njg5OTk5LCJzdWIiOiI2OGMwY2FjYmY0ZmM3NDRlNTE5OGM2NmUiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.hMpJWMjbeV_L14WlGEildsyP_PmQPDknj3M8FP7s02o")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            MovieList playingMovieList = mapper.readValue(response.body(), MovieList.class);
            List<Movie> results = playingMovieList.getResults();

            for (Movie movie : results) {
                movie.setPlaying(true);
                Optional<Movie> findMovie = movieRepository.findByMovieId(movie.getMovieId());
                if(findMovie.isEmpty()){
                    movieRepository.save(movie);
                    mappingGenreWithMovie(movie);
                }
                else{
                    Movie playingMovie = findMovie.get();
                    playingMovie.setPlaying(true);
                }
            }

            if(playingMovieList.getTotalPages() == i){
                break;
            }
        }
    }

    @Transactional
    public void mappingGenreWithMovie(Movie movie){
        List<Integer> genreIds = movie.getGenreIds();
        for (Integer genreId : genreIds) {
            MovieGenres movieGenres = new MovieGenres();
            Optional<Genre> findGenre = genreRepository.findByGenreId(genreId);
            if(findGenre.isEmpty()){
                throw new RuntimeException("해당 장르를 찾을 수 없습니다");
            }
            Genre genre = findGenre.get();
            movieGenres.setMovie(movie);
            movieGenres.setGenre(genre);
            movie.getMovieGenres().add(movieGenres);
            movieGenresRepository.save(movieGenres);
        }
    }
}
