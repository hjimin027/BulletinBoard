package com.bulletin.board;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bulletin.board.post.Post;
import com.bulletin.board.post.PostRepository;

@SpringBootTest
class BulletinBoardApplicationTests {

	@Autowired
	private PostRepository postRepository;
	
	@Test
	void testJpa() {
		Post p1 = new Post();
		p1.setTitle("1빠(test)");
		p1.setContent("첫 게시글입니다.(테스트)");
		p1.setCreateDate(LocalDateTime.now());
		this.postRepository.save(p1);
		
		Post p2 = new Post();
		p2.setTitle("2빠(test)");
		p2.setContent("두 번째 게시글입니다.(테스트)");
		p2.setCreateDate(LocalDateTime.now());
		this.postRepository.save(p2);
	}

}
