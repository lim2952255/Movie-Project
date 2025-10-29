package com.limhs.movie_project.interceptor;

import com.limhs.movie_project.config.springSecurity.CustomUserDetails;
import com.limhs.movie_project.domain.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    // 로그인 정보를 모델에 담는 역할을 수행한다.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if(modelAndView != null){
            log.info("modelAndView 뷰 이름: {}", modelAndView.getViewName());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {

            Object principal = authentication.getPrincipal();

            if (principal instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                User user = userDetails.getUser(); // User 객체 반환 메서드 필요
                if (modelAndView != null) {
                    modelAndView.addObject("user", user);
                    log.info("modelAndView에 로그인 정보 추가, {}", user.getUserId());
                }
            }
        }
    }
}
