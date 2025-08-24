package com.bulletin.board.bookmark;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bulletin.board.post.Post;
import com.bulletin.board.user.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService {
	private final BookmarkRepository bookmarkRepository;
	
	public void toggleBookmark(SiteUser user, Post post) {
		Optional<Bookmark> exist = bookmarkRepository.findByUserAndPost(user, post);
		exist.ifPresentOrElse(
				// Present: 이미 북마크되어있음, 북마크 삭제
				bookmarkRepository::delete, 
				// Else: 북마크 안되어있음, 북마크 추가
				() -> {
					Bookmark bookmark = new Bookmark();
					bookmark.setUser(user);
					bookmark.setPost(post);
					bookmark.setCreatedDate(LocalDateTime.now());
					this.bookmarkRepository.save(bookmark);
				}
			);
	}
	
	public List<Bookmark> getBookmarks(SiteUser user){
		return bookmarkRepository.findAllByUser(user);
	}
}
