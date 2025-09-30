package com.limhs.movie_project.domain.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieList {
    private int page;
    private List<Movie> results;

    @JsonProperty("total_pages")
    private int totalPages;
}
