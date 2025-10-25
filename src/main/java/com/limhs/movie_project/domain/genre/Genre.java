package com.limhs.movie_project.domain.genre;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.limhs.movie_project.domain.mappedSuperClass.BaseEntity;
import com.limhs.movie_project.domain.movie.MovieGenres;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY;

@Getter
@Setter
@Entity
@AttributeOverride(name = "id", column = @Column(name = "default_id"))
@org.hibernate.annotations.Cache(usage = READ_ONLY)
public class Genre extends BaseEntity {

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = READ_ONLY)
    private List<MovieGenres> movieGenres = new ArrayList<>();

    @JsonProperty("id")
    private int genreId;

    @JsonProperty("name")
    private String genreName;
}
