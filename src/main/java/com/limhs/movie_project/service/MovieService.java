package com.limhs.movie_project.service;

import com.limhs.movie_project.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;


}
