package com.limhs.movie_project.controller;

import com.limhs.movie_project.domain.User;
import com.limhs.movie_project.domain.movie.MovieCardDTO;
import com.limhs.movie_project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;

    @GetMapping //localhost:8080에 접속시 /home으로 리다이렉트 되도록 설정
    public String homeRedirect(){
        return "redirect:/home";
    }

    @GetMapping("home")
    public String home(Model model){
        return "home/home";
    }

    @GetMapping("mypage")
    public String mypage(HttpServletRequest request, HttpServletResponse response, Model model){

        List<MovieCardDTO> favoriteMovie = userService.getFavoriteMovie(request, response);

        model.addAttribute("movies", favoriteMovie);

        return "home/mypage.html";
    }
}
