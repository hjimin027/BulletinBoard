package com.bulletin.board.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Integer> {
	Post findByTitle(String title);
	Post findByTitleAndContent(String title, String content);
	List<Post> findByTitleLike(String title);
	Page<Post> findAll(Pageable pageable);
	Page<Post> findAll(Specification<Post> spec, Pageable pageable);
	List<Post> findByAuthor_Username(String username);
	
	@Query("SELECT p FROM Post p WHERE SIZE(p.voter) >= 10")
    List<Post> findBestPosts();
	
}
