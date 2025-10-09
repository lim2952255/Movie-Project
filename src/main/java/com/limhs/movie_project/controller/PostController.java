package com.limhs.movie_project.controller;

import com.limhs.movie_project.domain.User;
import com.limhs.movie_project.domain.post.Post;
import com.limhs.movie_project.service.LikeService;
import com.limhs.movie_project.service.PostService;
import com.limhs.movie_project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final LikeService likeService;

    @GetMapping("/create/{movieId}")
    public String postWrite(@PathVariable String movieId,Model model){
        model.addAttribute("post",new Post());
        model.addAttribute("movieId",movieId);
        return "post/write";
    }

    @PostMapping("/create/{movieId}")
    public String create(@Validated Post post, BindingResult bindingResult, @PathVariable String movieId, HttpServletRequest request, HttpServletResponse response, Model model){
        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "post/write";
        }

        Post savedPost = postService.savePost(post, movieId, request, response);

        model.addAttribute("post", savedPost);

        return "redirect:/movie/"+movieId;
    }

    @GetMapping("/update/{movieId}/{postId}")
    public String update(@PathVariable String movieId,@PathVariable String postId, Model model){
        long id = Long.parseLong(postId);

        Post post = postService.findPost(id);

        model.addAttribute("post",post);
        model.addAttribute("movieId",movieId);
        return "post/update";
    }

    @PostMapping("/update/{movieId}/{postId}")
    public String updatePost(@Validated Post post, BindingResult bindingResult , @PathVariable String movieId ,@PathVariable String postId, Model model){
        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "post/update";
        }

        long id = Long.parseLong(postId);

        Post updatePost = postService.updatePost(post, id);

        model.addAttribute("post", updatePost);

        return "redirect:/movie/"+ movieId;
    }
    @GetMapping("/delete/{movieId}/{postId}")
    public String delete(@PathVariable String movieId, @PathVariable String postId){
        Long id = Long.parseLong(postId);

        postService.deletePost(id);

        return "redirect:/movie/"+movieId;
    }

    @GetMapping("/{postId}")
    public String joinPost(@PathVariable String postId, HttpServletRequest request, HttpServletResponse response ,Model model){
        long id = Long.parseLong(postId);
        Post post = postService.findPost(id);
        User user = userService.getUser(request, response);

        boolean like = likeService.userLikesPost(post, user);
        model.addAttribute("post",post);
        model.addAttribute("userLike",like);
        return "post/post";
    }
}
