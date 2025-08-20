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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	// 로그인 사용자
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
	
	//비로그인 사용자
	@PostMapping("/createGuest/{id}")
	public String createGuestComment(Model model, @PathVariable("id") Integer id,
			@Valid CommentForm commentForm, BindingResult bindingResult) {
	    Post post = this.postService.getPost(id);
	    if (bindingResult.hasErrors() || commentForm.getGuestName() == null || commentForm.getGuestPassword() == null) {
	        model.addAttribute("post", post);
	        return "post_detail";
	    }
	    this.commentService.createAsGuest(post, commentForm.getContent(),
	                                      commentForm.getGuestName(), commentForm.getGuestPassword());
	    return String.format("redirect:/post/detail/%s", id);
	}
	
	// 로그인 사용자
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
	
	//비로그인 사용자
	@GetMapping("/deleteGuest/{id}")
	public String deleteGuestComment(@PathVariable("id") Integer id, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
	    Comment comment = this.commentService.getComment(id);
	    if (comment.getAuthor() != null) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원 댓글은 이 경로로 삭제 불가");
	    }
	    if (!comment.getGuestPassword().equals(password)) {
	    	redirectAttributes.addAttribute("error", "password");
	        return "redirect:/question/detail/" + comment.getPost().getId();
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
