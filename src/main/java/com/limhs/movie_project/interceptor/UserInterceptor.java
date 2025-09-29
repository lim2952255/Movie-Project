package com.limhs.movie_project.interceptor;

import com.limhs.movie_project.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    // 로그인 정보를 모델에 담는 역할을 수행한다.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        HttpSession session = request.getSession(false);

        if(modelAndView != null){
            log.info("modelAndView 뷰 이름: {}", modelAndView.getViewName());
        }

        if(session != null){
            Object findUser = session.getAttribute("user");
            if(findUser != null){
                User user = (User) findUser;
                modelAndView.addObject("user",user);
                log.info("modelAndView에 로그인 정보 추가,{}",user.getUserId());
            }
        }
    }
}
