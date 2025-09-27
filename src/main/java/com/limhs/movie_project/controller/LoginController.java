package com.limhs.movie_project.controller;

import com.limhs.movie_project.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/login/")
@Slf4j
public class LoginController {

    @GetMapping("signup")
    public String signup(Model model){
        model.addAttribute("user", new User());
        return "login/signup";
    }

    @PostMapping("signup")
    public String signupForm(@Validated @ModelAttribute("user") User userForm, BindingResult bindingResult, RedirectAttributes redirect, Model model){
        // 검증 오류 발생
        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "login/signup";
        }

        User user = new User(userForm);
        //redirect.addAttribute("userId", user.getUserId());
        return "redirect:/home";
    }
}
