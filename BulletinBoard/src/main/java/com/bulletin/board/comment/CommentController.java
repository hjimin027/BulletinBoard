package com.bulletin.board.comment;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.bulletin.board.post.Post;
import com.bulletin.board.post.PostService;
import com.bulletin.board.user.SiteUser;
import com.bulletin.board.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;
	private final PostService postService;
	private final UserService userService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createComment(Model model, @PathVariable("id") Integer id, 
			@Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
		Post post = this.postService.getPost(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		if (bindingResult.hasErrors()) {
			model.addAttribute("post", post);
			return "post_detail";
		}
		this.commentService.create(post, commentForm.getContent(), siteUser);
		return String.format("redirect:/post/detail/%s", id);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String commentDelete(Principal principal, @PathVariable("id") Integer id) {
		Comment comment = this.commentService.getComment(id);
		if (!comment.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.commentService.delete(comment);
		return String.format("redirect:/post/detail/%s", comment.getPost().getId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String commentVote(Principal principal, @PathVariable("id") Integer id) {
		Comment comment = this.commentService.getComment(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.commentService.vote(comment, siteUser);
		return String.format("redirect:/post/detail/%s", comment.getPost().getId());
	}
}
