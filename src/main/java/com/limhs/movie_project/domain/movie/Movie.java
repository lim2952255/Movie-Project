package com.limhs.movie_project.domain.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.limhs.movie_project.domain.converter.MovieUrlConverter;
import com.limhs.movie_project.domain.favorite.Favorite;
import com.limhs.movie_project.domain.mappedSuperClass.BaseEntity;
import com.limhs.movie_project.domain.post.Post;
import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AttributeOverride(name = "id", column = @Column(name = "default_id"))
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Movie extends BaseEntity {
    public Movie() {
    }

    private boolean adult = true;

    private boolean isPlaying = false;

    private boolean isPopular = false;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieGenres> movieGenres = new ArrayList<>();

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Favorite> favorites = new ArrayList<>();

    @JsonProperty("genre_ids")
    private List<Integer> genreIds;

    @JsonProperty("id")
    @Column(unique = true)
    private int movieId;

    @JsonProperty("overview")

    @Column(columnDefinition = "LONGTEXT")
    private String overview;

    private Double popularity;

    @JsonProperty("poster_path")
    @Convert(converter = MovieUrlConverter.class)
    private String posterPath;

    @JsonProperty("release_date")
    private String releaseDate;

    private String title;

    @Version
    private Long version = 0L; // 초기화 명시



}
