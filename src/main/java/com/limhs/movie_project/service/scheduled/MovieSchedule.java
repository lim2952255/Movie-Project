package com.limhs.movie_project.service.scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.limhs.movie_project.domain.genre.Genre;
import com.limhs.movie_project.domain.genre.GenreList;
import com.limhs.movie_project.domain.movie.*;
import com.limhs.movie_project.repository.movie.GenreRepository;
import com.limhs.movie_project.repository.movie.MovieGenresRepository;
import com.limhs.movie_project.repository.movie.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

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
    @Scheduled(cron = "0 1 19 * * *")
    @Transactional
    public void movieListUpdate() throws IOException, InterruptedException {
        log.info("Genre Update start");
        getGenres();
        log.info("Genre Update end");

        log.info("Update start");

        List<Integer> movieList = movieRepository.findByIsPlayingTrueOrIsPopularTrue()
                .stream().map(movie -> movie.getMovieId()).toList();
        
        movieRepository.resetAllFlags(); // 벌크연산 수행시 영속성 컨텍스트 플러시

        Map<Integer, Movie> movieMap = movieRepository.findByMovieIdIn(movieList).stream()
                .collect(Collectors.toMap(movie -> movie.getMovieId(), movie -> movie)); // 다시 영속화

        List<Movie> newSavedMovie = new ArrayList<>();

        getMoviesFromTMDBAPI(MovieType.PLAYING, movieMap, newSavedMovie);

        getMoviesFromTMDBAPI(MovieType.POPULAR, movieMap, newSavedMovie);

        if(!newSavedMovie.isEmpty()){
            List<Movie> movies = movieRepository.saveAll(newSavedMovie);

            mappingGenreWithMovie(movies);
        }

        log.info("Update end");
    }

    @Transactional
    public void getGenres() throws IOException, InterruptedException {
        HttpRequest request;
        HttpResponse<String> response;
        ObjectMapper mapper = new ObjectMapper();

        request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/genre/movie/list?language=ko"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " +tmdbApiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        GenreList genreList = mapper.readValue(response.body(), GenreList.class);
        List<Genre> genres = genreList.getGenres();

        Map<Integer, Genre> genreMap = genreRepository.findAll().stream()
                .collect(Collectors.toMap(genre -> genre.getGenreId(), genre -> genre));
        for (Genre genre : genres) {
            if(genreMap.get(genre.getGenreId()) == null){
                genreRepository.save(genre);
            }
        }
    }

    @Transactional
    public void getMoviesFromTMDBAPI(MovieType movieType, Map<Integer, Movie> movieMap, List<Movie> newSavedMovie) throws IOException, InterruptedException {
        HttpRequest request;
        HttpResponse<String> response;
        ObjectMapper mapper = new ObjectMapper();

        String url;

        if(movieType.equals(MovieType.PLAYING)){
            url = "https://api.themoviedb.org/3/movie/now_playing?language=ko&region=KR&page=";
        } else{
            url = "https://api.themoviedb.org/3/movie/popular?language=en-US&page=";
        }

        for (int i = 1; i <= 5; i++) {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url + i))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + tmdbApiKey)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            MovieList movieList = mapper.readValue(response.body(), MovieList.class);
            List<Movie> results = movieList.getResults();

            for (Movie movie : results) {
                if(movieType.equals(MovieType.PLAYING)){
                    movie.setPlaying(true);
                } else{
                    movie.setPopular(true);
                }
                Movie findMovie = movieMap.get(movie.getMovieId());
                if(findMovie == null){
                    movieMap.put(movie.getMovieId(), movie); //중복 처리
                    newSavedMovie.add(movie);
                }
                else{
                    if(movieType.equals(MovieType.PLAYING)){
                        findMovie.setPlaying(true);
                    } else{
                        findMovie.setPopular(true);
                    }
                }
            }
            if(movieList.getTotalPages() == i){
                break;
            }
        }
    }

    @Transactional
    public void mappingGenreWithMovie(List<Movie> movieList){
        Map<Integer, Genre> genreMap = genreRepository.findAll().stream()
                .collect(Collectors.toMap(genre -> genre.getGenreId(), genre -> genre));

        Map<Long, List<Genre>> movieGenresMap = movieGenresRepository.findAll().stream() // 중복체크용
                .collect(Collectors.groupingBy(
                        movieGenre -> movieGenre.getMovie().getId(), // key: movie_id
                        Collectors.mapping(
                                movieGenre -> movieGenre.getGenre(), // value: Genre 객체
                                Collectors.toList()                  // value를 리스트로 수집
                        )
                ));

        List<MovieGenres> movieGenresList = new ArrayList<>();

        for (Movie movie : movieList) {

            List<Integer> genreIds = movie.getGenreIds(); // 새로운 영화와 매핑되어있는 genreIds
            List<Genre> genres = movieGenresMap.get(movie.getId()); // 현재 DB에서 영화와 매핑되어 있는 genres

            for (Integer genreId : genreIds) {
                Genre savedGenre = genreMap.get(genreId);
                boolean duplicateCheck = false;

                if(genres != null){
                    for (Genre genre : genres) { // 새로운 영화와 장르 정보가 이미 DB에 저장되어 있는지 확인
                        if(genre.getGenreId() == savedGenre.getGenreId()){
                            duplicateCheck = true;
                            break;
                        }
                    }
                }
                if(!duplicateCheck){
                    MovieGenres movieGenres = new MovieGenres();
                    movieGenres.setMovieGenres(movie,savedGenre);

                    movieGenresList.add(movieGenres);
                }
            }
        }
        if(!movieGenresList.isEmpty()){
            movieGenresRepository.saveAll(movieGenresList);
        }
    }
}
