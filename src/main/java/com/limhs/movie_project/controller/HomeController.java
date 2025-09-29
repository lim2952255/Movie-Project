package com.limhs.movie_project.controller;

import com.limhs.movie_project.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping //localhost:8080에 접속시 /home으로 리다이렉트 되도록 설정
    public String homeRedirect(){
        return "redirect:/home";
    }

    @GetMapping("home")
    public String home(@SessionAttribute(name = "user", required = false) User user, Model model){
        log.info("home 컨트롤러 호출");

        if(user != null){
            model.addAttribute("user",user);
            log.info("로그인 정보 추가");
        }

        return "home";
    }
}
