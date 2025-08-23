package com.bulletin.board.comment;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bulletin.board.DataNotFoundException;
import com.bulletin.board.post.Post;
import com.bulletin.board.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	private final CommentRepository commentRepository;
	private final PasswordEncoder passwordEncoder;
	
	public void create(Post post, String content, SiteUser author) {
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setCreateDate(LocalDateTime.now());
		comment.setPost(post);
		comment.setAuthor(author);
		this.commentRepository.save(comment);
	}
	
	public void createAsGuest(Post post, String content, String guestName, String guestPassword) {
	    Comment comment = new Comment();
	    comment.setContent(content);
	    comment.setCreateDate(LocalDateTime.now());
	    comment.setPost(post);
	    comment.setGuestName(guestName);
	    comment.setGuestPassword(guestPassword, passwordEncoder);
	    this.commentRepository.save(comment);
	}
	
	public void delete(Comment comment) {
		this.commentRepository.delete(comment);
	}
	
	public void deleteGuestComment(Integer id, String inputPassword) {
        Comment comment = getComment(id);
        if (!comment.checkPassword(inputPassword, passwordEncoder)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        commentRepository.delete(comment);
    }

	public Comment getComment(Integer id) {
		Optional<Comment> comment = this.commentRepository.findById(id);
		if (comment.isPresent()) {
			return comment.get();
		} else {
			throw new DataNotFoundException("comment not found");
		}
	}
	
	public void vote(Comment comment, SiteUser siteUser) {
		comment.getVoter().add(siteUser);
		this.commentRepository.save(comment);
	}
}
