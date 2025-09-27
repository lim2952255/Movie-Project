package com.limhs.movie_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping //localhost:8080에 접속시 /home으로 리다이렉트 되도록 설정
    public String homeRedirect(){
        return "redirect:/home";
    }

    @GetMapping("home")
    public String home(){
        return "home";
    }
}
