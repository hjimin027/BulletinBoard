package com.bulletin.board.post;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PostController {
	private final PostService postService;
	
	@GetMapping("/post/list")
	public String list(Model model) {
		List<Post> postList = this.postService.getList();
		model.addAttribute("postList", postList);
		return "post_list";
	}
	
	@GetMapping(value = "/post/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
		Post post = this.postService.getPost(id);
        model.addAttribute("post", post);
        return "post_detail";
    }
}
