package com.limhs.movie_project.controller.comment;

import com.limhs.movie_project.service.like.CommentLikeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @GetMapping("/commentLike/{postId}/{commentId}")
    public String commentLike(@PathVariable String postId, @PathVariable String commentId, HttpServletRequest request, HttpServletResponse response){
        Long id = Long.parseLong(commentId);

        commentLikeService.setCommentLike(id, request, response);

        return "redirect:/post/"+ postId;
    }

    @GetMapping("/commentDislike/{postId}/{commentId}")
    public String commentDislike(@PathVariable String postId, @PathVariable String commentId, HttpServletRequest request, HttpServletResponse response){
        Long id = Long.parseLong(commentId);

        commentLikeService.deleteCommentLike(id, request, response);

        return "redirect:/post/"+ postId;
    }
}
