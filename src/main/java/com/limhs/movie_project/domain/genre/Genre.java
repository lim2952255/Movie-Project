package com.limhs.movie_project.domain.genre;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.limhs.movie_project.domain.movie.MovieGenres;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long defaultId;

    @OneToMany(mappedBy = "genre")
    private List<MovieGenres> movieGenres;

    @JsonProperty("id")
    private int genreId;

    @JsonProperty("name")
    private String genreName;
}
