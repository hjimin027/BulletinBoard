package com.bulletin.board;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bulletin.board.comment.Comment;
import com.bulletin.board.comment.CommentRepository;
import com.bulletin.board.post.Post;
import com.bulletin.board.post.PostRepository;

@SpringBootTest
class BulletinBoardApplicationTests {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Test
	void testJpa() {
//		Post p1 = new Post();
//		p1.setTitle("1빠(te0st)");
//		p1.setContent("첫 게시글입니다.(테스트)");
//		p1.setCreateDate(LocalDateTime.now());
//		this.postRepository.save(p1);
//		
//		Post p2 = new Post();
//		p2.setTitle("2빠(test)");
//		p2.setContent("두 번째 게시글입니다.(테스트)");
//		p2.setCreateDate(LocalDateTime.now());
//		this.postRepository.save(p2);
		
		Optional<Post> op = this.postRepository.findById(2);
		assertTrue(op.isPresent());
		Post p = op.get();
		
		Comment c = new Comment();
		c.setContent("1빠 답변");
		c.setPost(p);
		c.setCreateDate(LocalDateTime.now());
		this.commentRepository.save(c);
	}

}
