package com.bulletin.board.bookmark;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.bulletin.board.post.Post;
import com.bulletin.board.post.PostService;
import com.bulletin.board.user.SiteUser;
import com.bulletin.board.user.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookmarkController {
	private final BookmarkService bookmarkService;
	private final PostService postService;
	private final UserService userService;
	
	@PostMapping("/post/{postId}/toggle")
	public String toggleBookmark(@PathVariable("postId") int postId, Principal principal) {
		SiteUser user = userService.getUser(principal.getName());
		Post post = postService.getPost(postId);
		bookmarkService.toggleBookmark(user, post);
		return "redirect:/post/detail/" + postId;
	}
	
	@GetMapping("/menu/bookmark")
	public String bookmark(Model model, Principal principal) {
		SiteUser user = userService.getUser(principal.getName());
		model.addAttribute("bookmarks", bookmarkService.getBookmarks(user));
    	return "menu_bookmark";
    }
}