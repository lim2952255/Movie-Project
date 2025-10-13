package com.limhs.movie_project.domain.genre;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.limhs.movie_project.domain.mappedSuperClass.BaseEntity;
import com.limhs.movie_project.domain.movie.MovieGenres;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@AttributeOverride(name = "id", column = @Column(name = "default_id"))
public class Genre extends BaseEntity {

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieGenres> movieGenres;

    @JsonProperty("id")
    private int genreId;

    @JsonProperty("name")
    private String genreName;
}
