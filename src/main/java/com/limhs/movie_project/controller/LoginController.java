package com.limhs.movie_project.controller;

import com.limhs.movie_project.domain.LoginDTO;
import com.limhs.movie_project.domain.User;
import com.limhs.movie_project.exception.DuplicatedUserId;
import com.limhs.movie_project.exception.LoginFailException;
import com.limhs.movie_project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/login/")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final UserService userService;

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

        try{
            userService.save(user);
        } catch(DuplicatedUserId e){
            bindingResult.addError(new FieldError("user","userId",userForm.getUserId(),false,null,null, e.getMessage()));
            log.info("errors={}",bindingResult);
            return "login/signup";
        }

        log.info("save success");

        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value="redirectURL", required = false) String redirectURL,
                        @RequestParam(value="message", required = false) String message,
                        Model model){
        //request.getAttribute("")
        model.addAttribute("loginDTO",new LoginDTO());
        model.addAttribute("redirectURL", redirectURL);
        if(message != null){
            model.addAttribute("message","해당 서비스는 로그인이 필요한 서비스입니다.");
        }
        return "login/login";
    }

    @PostMapping("/login")
    public String loginForm(@Validated @ModelAttribute LoginDTO loginDTO, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirect, Model model){
        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "login/login";
        }
        User user;

        try {
            // 로그인 검증
            user = userService.login(loginDTO);
        } catch(LoginFailException e){
            // 로그인 실패 로직
            bindingResult.addError(new ObjectError("loginDTO", e.getMessage()));
            log.info("errors={}",bindingResult);
            return "login/login";
        }

        // 로그인 성공 로직
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        String redirectURL = request.getParameter("redirectURL");

        if(redirectURL != null){
            return "redirect:"+redirectURL;
        }
        return "redirect:/";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);
        session.invalidate();

        return "redirect:/home";
    }
}
