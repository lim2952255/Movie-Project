package com.limhs.movie_project.interceptor;

import com.limhs.movie_project.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if(session == null){
            String redirectURL = request.getRequestURI();
            String message = "loginFirst";
            response.sendRedirect("/login/login?"+"redirectURL="+redirectURL+"&message="+message);
            return false;
        }
        return true;
    }
}
