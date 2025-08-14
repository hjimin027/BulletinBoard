package com.bulletin.board.post;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bulletin.board.user.SiteUser;
import com.bulletin.board.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PostController {
	private final PostService postService;
	private final UserService userService;
	
	@GetMapping("/post/list")
	public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
		Page<Post> paging = this.postService.getList(page);
		model.addAttribute("paging", paging);
		return "post_list";
	}
	
	@GetMapping(value = "/post/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
		Post post = this.postService.getPost(id);
        model.addAttribute("post", post);
        return "post_detail";
    }

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/post/create")
    public String postCreate(PostForm postForm) {
        return "post_form";
    }

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/post/create")
    public String postCreate(@Valid PostForm postForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "post_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.postService.create(postForm.getTitle(), postForm.getContent(), siteUser);
        return "redirect:/post/list";
    }
}
