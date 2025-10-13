package com.limhs.movie_project.controller.like;

import com.limhs.movie_project.service.like.LikeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @GetMapping("/like/{postId}")
    public String like(@PathVariable String postId, HttpSession session){
        long id = Long.parseLong(postId);

        likeService.saveLike(id, session);

        return "redirect:/post/"+postId;
    }

    @GetMapping("/dislike/{postId}")
    public String dislike(@PathVariable String postId,HttpSession session){
        long id = Long.parseLong(postId);

        likeService.deleteLike(id, session);

        return "redirect:/post/"+postId;
    }
}
