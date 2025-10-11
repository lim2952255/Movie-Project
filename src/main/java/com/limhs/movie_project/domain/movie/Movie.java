package com.limhs.movie_project.domain.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.limhs.movie_project.domain.favorite.Favorite;
import com.limhs.movie_project.domain.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    public Movie() {
    }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long defaultId;

    private boolean adult = true;

    private boolean isPlaying = false;

    private boolean isPopular = false;

    @OneToMany(mappedBy = "movie")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    private List<MovieGenres> movieGenres = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    private List<Favorite> favorites = new ArrayList<>();

    @JsonProperty("genre_ids")
    private List<Integer> genreIds;

    @JsonProperty("id")
    private int movieId;

    @JsonProperty("overview")

    @Column(columnDefinition = "LONGTEXT")
    private String overview;

    private Double popularity;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("release_date")
    private String releaseDate;

    private String title;

}
