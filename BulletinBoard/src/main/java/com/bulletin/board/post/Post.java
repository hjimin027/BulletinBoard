package com.bulletin.board.post;

import java.time.LocalDateTime;
import java.util.List;

import com.bulletin.board.comment.Comment;
import com.bulletin.board.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Set;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 200)
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	@Column(columnDefinition = "integer default 0", nullable = false)
	private int view;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
	private List<Comment> commentList;
	
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifyDate;
	
	@ManyToMany
    Set<SiteUser> voter;
	
	@Column(columnDefinition = "integer default 0", nullable = false)
	private int view;
}
