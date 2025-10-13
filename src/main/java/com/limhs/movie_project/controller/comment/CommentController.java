package com.limhs.movie_project.controller.comment;

import com.limhs.movie_project.domain.comment.Comment;
import com.limhs.movie_project.service.comment.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{postId}")
    public String commentForm(@PathVariable String postId, Model model){
        Comment comment = new Comment();
        model.addAttribute("comment",comment);
        model.addAttribute("postId",postId);

        return "comment/comment";
    }

    @PostMapping("/{postId}")
    public String commentSave(@Validated Comment comment, BindingResult bindingResult, @PathVariable String postId, HttpSession session){
        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "comment/comment";
        }
        Long id = Long.parseLong(postId);

        commentService.saveComment(comment,id,session);

        return "redirect:/post/"+postId;
    }

    @GetMapping("/update/{postId}/{commentId}")
    public String updateForm(@PathVariable String postId, @PathVariable String commentId, Model model){
        Long id = Long.parseLong(commentId);
        Comment comment = commentService.findComment(id);

        model.addAttribute("comment",comment);
        model.addAttribute("postId",postId);

        return "comment/updatecomment";
    }

    @PostMapping("/update/{postId}/{commentId}")
    public String updateComment(@Validated Comment comment, BindingResult bindingResult ,@PathVariable String postId, @PathVariable String commentId){
        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "comment/updatecomment";
        }
        Long id = Long.parseLong(commentId);

        commentService.updateComment(comment, id);

        return "redirect:/post/"+ postId;
    }

    @GetMapping("/delete/{postId}/{commentId}")
    public String deleteComment(@PathVariable String postId, @PathVariable String commentId){
        Long id = Long.parseLong(commentId);

        commentService.deleteComment(id);

        return "redirect:/post/"+ postId;
    }

}
