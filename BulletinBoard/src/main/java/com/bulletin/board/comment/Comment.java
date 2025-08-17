package com.bulletin.board.comment;

import java.time.LocalDateTime;
import java.util.Set;

import com.bulletin.board.post.Post;
import com.bulletin.board.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	@ManyToOne
	private Post post;
	
	@ManyToOne
	private SiteUser author;
	
	@ManyToMany
	Set<SiteUser> voter;
}
