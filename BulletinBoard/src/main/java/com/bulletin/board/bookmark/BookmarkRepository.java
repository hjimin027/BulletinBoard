package com.bulletin.board.bookmark;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bulletin.board.post.Post;
import com.bulletin.board.user.SiteUser;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
	List<Bookmark> findAllByUser(SiteUser user);
	Optional<Bookmark> findByUserAndPost(SiteUser user, Post post);
}
