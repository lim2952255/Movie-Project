package com.limhs.movie_project.controller;

import com.limhs.movie_project.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @GetMapping("/like/{postId}")
    public String like(@PathVariable String postId, HttpServletRequest request, HttpServletResponse response){
        long id = Long.parseLong(postId);

        likeService.saveLike(id, request, response);

        return "redirect:/post/"+postId;
    }

    @GetMapping("/dislike/{postId}")
    public String dislike(@PathVariable String postId, HttpServletRequest request, HttpServletResponse response){
        long id = Long.parseLong(postId);

        likeService.deleteLike(id, request, response);

        return "redirect:/post/"+postId;
    }
}
