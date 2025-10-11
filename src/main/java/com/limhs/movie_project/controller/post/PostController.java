package com.limhs.movie_project.controller.post;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.domain.comment.CommentDTO;
import com.limhs.movie_project.domain.post.Post;
import com.limhs.movie_project.service.comment.CommentService;
import com.limhs.movie_project.service.like.LikeService;
import com.limhs.movie_project.service.post.PostService;
import com.limhs.movie_project.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final LikeService likeService;
    private final CommentService commentService;

    @GetMapping("/{postId}")
    public String redirectPost(@PathVariable String postId){
        return "redirect:/post/"+postId+"/1";
    }

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

    @GetMapping("/{postId}/{pageNumber}")
    public String joinPost(@PathVariable String postId, @PathVariable String pageNumber ,HttpServletRequest request, HttpServletResponse response ,Model model){
        long id = Long.parseLong(postId);
        int number = Integer.parseInt(pageNumber) - 1;

        Post post = postService.findPost(id);
        User user = userService.getUser(request, response);

        boolean like = likeService.userLikesPost(post, user);

        Page<CommentDTO> findComments = commentService.findComment(id, number);
        List<CommentDTO> commentList = findComments.getContent();

        List<CommentDTO> comments = commentService.userLikesComment(commentList, user);

        model.addAttribute("post",post);
        model.addAttribute("userLike",like);

        model.addAttribute("comments",comments);
        model.addAttribute("totalPages", findComments.getTotalPages());
        model.addAttribute("currentPage", number + 1);

        return "post/post";
    }
}
