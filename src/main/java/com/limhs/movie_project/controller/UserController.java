package com.limhs.movie_project.controller;

import com.limhs.movie_project.domain.movie.MovieCardDTO;
import com.limhs.movie_project.service.FavoriteService;
import com.limhs.movie_project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final FavoriteService favoriteService;

    @GetMapping("mypage/favorites")
    public String favoritesRedirect(){
        return "redirect:/mypage/favorites/1";
    }

    @GetMapping("mypage/favorites/{pageNumber}")
    public String favorites(@PathVariable String pageNumber, HttpServletRequest request, HttpServletResponse response, Model model){
        int number = Integer.parseInt(pageNumber) - 1   ;

        Page<MovieCardDTO> favoriteMovie = favoriteService.getFavoriteMovie(request, response, number);
        List<MovieCardDTO> movies = favoriteMovie.getContent();

        model.addAttribute("movies", movies);
        model.addAttribute("totalPages", favoriteMovie.getTotalPages());
        model.addAttribute("currentPage", number + 1);

        return "home/favoritelist.html";
    }

    @PostMapping("user/{movieId}/favorite")
    public String favorite(@PathVariable String movieId, HttpServletRequest request, HttpServletResponse response, Model model){
        int movieNum = Integer.parseInt(movieId);

        favoriteService.setFavorites(movieNum,request,response);

        return "redirect:/movie/"+movieId;
    }

    @PostMapping("user/{movieId}/unfavorite")
    public String unfavorite(@PathVariable String movieId, HttpServletRequest request, HttpServletResponse response, Model model){
        int movieNum = Integer.parseInt(movieId);

        favoriteService.unsetFavorites(movieNum,request,response);

        return "redirect:/movie/"+movieId;
    }
}
