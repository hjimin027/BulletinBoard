package com.bulletin.board.comment;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.bulletin.board.post.Post;
import com.bulletin.board.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	private final CommentRepository commentRepository;
	
	public void create(Post post, String content, SiteUser author) {
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setCreateDate(LocalDateTime.now());
		comment.setPost(post);
		comment.setAuthor(author);
		this.commentRepository.save(comment);
	}
}
