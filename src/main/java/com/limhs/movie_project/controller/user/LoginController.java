package com.limhs.movie_project.controller.user;

import com.limhs.movie_project.domain.LoginDTO;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.exception.DuplicatedUserId;
import com.limhs.movie_project.exception.LoginFailException;
import com.limhs.movie_project.service.user.UserService;
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
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("user", new User());
        return "login/signup";
    }

    @PostMapping("/signup")
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
    public String login( @RequestParam(value="message", required = false) String message,
                         @RequestParam(value="error", required = false) String error,
                        Model model){
        model.addAttribute("loginDTO",new LoginDTO());
        if(message != null){
            model.addAttribute("message","해당 서비스는 로그인이 필요한 서비스입니다.");
        }
        if(error != null){
            model.addAttribute("validationError", "아이디 또는 비밀번호가 일치하지 않습니다");
        }

        return "login/login";
    }
}
