package com.bulletin.board.comment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bulletin.board.post.Post;
import com.bulletin.board.post.PostService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;
	private final PostService postService;
	
	@PostMapping("/create/{id}")
	public String createComment(Model model, @PathVariable("id") Integer id, @RequestParam(value="content") String content) {
		Post post = this.postService.getPost(id);
		this.commentService.create(post, content);
		return String.format("redirect:/post/detail/%s", id);
	}
}
