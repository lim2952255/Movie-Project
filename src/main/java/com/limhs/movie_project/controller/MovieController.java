package com.limhs.movie_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @GetMapping("/popularList")
    public String popularList(){
        return "/movie/popularList";
    }

    @GetMapping("/playingList")
    public String playingList(){
        return "/movie/playingList";
    }
}
