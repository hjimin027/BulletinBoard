package com.bulletin.board.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
	Post findByTitle(String title);
	Post findByTitleAndContent(String title, String content);
	List<Post> findByTitleLike(String title);
	Page<Post> findAll(Pageable pageable);
}
