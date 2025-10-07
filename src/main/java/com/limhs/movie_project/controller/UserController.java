package com.limhs.movie_project.controller;

import com.limhs.movie_project.domain.movie.MovieCardDTO;
import com.limhs.movie_project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/{movieId}/favorite")
    public String favorite(@PathVariable String movieId, HttpServletRequest request, HttpServletResponse response, Model model){
        int movieNum = Integer.parseInt(movieId);

        userService.setFavorites(movieNum,request,response);

        return "redirect:/movie/"+movieId;
    }

    @PostMapping("/{movieId}/unfavorite")
    public String unfavorite(@PathVariable String movieId, HttpServletRequest request, HttpServletResponse response, Model model){
        int movieNum = Integer.parseInt(movieId);

        userService.unsetFavorites(movieNum,request,response);

        return "redirect:/movie/"+movieId;
    }
}
