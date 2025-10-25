package com.limhs.movie_project.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Instant start = Instant.now();
        request.setAttribute("startTime", start); // 필드 대신 request 속성 사용
        log.info("컨트롤러 호출전, 요청 url: {}", request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        Instant start = (Instant) request.getAttribute("startTime");
        Instant post = Instant.now();
        request.setAttribute("postTime", post); // 필드 대신 request 속성 사용
        log.info("컨트롤러 호출간 작업시간: {}ms", Duration.between(start, post).toMillis());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Instant start = (Instant) request.getAttribute("startTime");
        Instant post =(Instant) request.getAttribute("postTime");
        Instant end = Instant.now();

        log.info("뷰 렌더링간 작업시간: {}ms", Duration.between(post, end).toMillis());
        log.info("총 작업시간: {}ms", Duration.between(start, end).toMillis());
    }
}
